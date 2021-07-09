package com.hokage.cache;

import com.google.common.cache.Cache;
import com.hokage.biz.Constant;
import com.hokage.biz.interceptor.UserContext;
import com.hokage.infra.worker.MasterThreadPoolWorker;
import com.hokage.infra.worker.ScheduledThreadPoolWorker;
import com.hokage.infra.worker.ThreadPoolWorker;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dao.HokageServerReportHandlerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageServerReportInfoHandlerDO;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.context.SshContext;
import com.hokage.ssh.enums.JSchChannelType;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/05/29 8:11 am
 * @description
 **/
@Slf4j
@DependsOn("sqlScriptInitRunner")
@Component
public class HokageServerCacheDao extends BaseCacheDao {

    @Value("${ssh.client.cache.refresh.interval.second}")
    private Integer cacheRefreshIntervalSecond;

    /**
     * key: ip_port_account
     * value: SshClient
     */
    private Cache<String, SshClient> serverKey2SshExecClient;
    /**
     * key: ip_port_account
     * value: SshClient
     */
    private Cache<String, SshClient> serverKey2SshSftpClient;

    /**
     * key: server
     * value: SshClient
     */
    @Getter
    private Cache<String, SshClient> server2MetricClient;

    private HokageServerDao serverDao;
    private CommandDispatcher dispatcher;

    private ScheduledThreadPoolWorker scheduledWorker;
    private ThreadPoolWorker poolWorker;
    private MasterThreadPoolWorker masterPoolWorker;
    private HokageServerReportHandlerDao reportHandlerDao;

    @Autowired
    public void setServerDao(HokageServerDao serverDao) {
        this.serverDao = serverDao;
    }

    @Autowired
    public void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    public void setScheduledWorker(ScheduledThreadPoolWorker scheduledWorker) {
        this.scheduledWorker = scheduledWorker;
    }

    @Autowired
    public void setPoolWorker(ThreadPoolWorker poolWorker) {
        this.poolWorker = poolWorker;
    }

    @Autowired
    public void setMasterPoolWorker(MasterThreadPoolWorker masterPoolWorker) {
        this.masterPoolWorker = masterPoolWorker;
    }

    @Autowired
    public void setReportHandlerDao(HokageServerReportHandlerDao reportHandlerDao) {
        this.reportHandlerDao = reportHandlerDao;
    }

    @PostConstruct
    public void init() {
        serverKey2SshExecClient = buildDefaultLocalCache();
        serverKey2SshSftpClient = buildDefaultLocalCache();
        server2MetricClient = buildDefaultLocalCache();
        scheduledWorker.getScheduledService().scheduleAtFixedRate(() -> {
            try {
                this.activeCacheRefresh();
            } catch (Exception e) {
                log.error("activeCacheRefresh scheduled error. errMsg: {}", e.getMessage());
            }
        }, 0, cacheRefreshIntervalSecond, TimeUnit.SECONDS);
    }

    public Optional<SshClient> getExecClient(String key) {
        SshClient client = serverKey2SshExecClient.getIfPresent(key);
        if (Objects.nonNull(client)) {
            return Optional.of(client);
        }
        return Optional.empty();
    }

    public Optional<SshClient> getSftpClient(String key) {
        SshClient client = serverKey2SshSftpClient.getIfPresent(key);
        if (Objects.nonNull(client)) {
            return Optional.of(client);
        }
        return Optional.empty();
    }



    /**
     * build cache key
     * @param serverDO server object
     * @return ip_port_account
     */
    private String buildKey(HokageServerDO serverDO) {
        return buildKey(serverDO.getIp(), serverDO.getSshPort(), serverDO.getAccount());
    }

    public String buildKey(String ip, String port, String account) {
        return ip + "_" + port + "_" + account;
    }

    /**
     * refresh server ssh client local cache
     */
    private void activeCacheRefresh() {
        UserContext ctx = UserContext.ctx();
        List<HokageServerDO> serverDOList = Objects.isNull(ctx.getUserId()) ? new ArrayList<>() : serverDao.selectByUserId(ctx.getUserId());
        log.info("my server size: {}", serverDOList.size());

        // 缓存exec client
        poolWorker.getExecutorPool().execute(() -> {
            this.activeNewServerCache(serverDOList, serverKey2SshExecClient, null);
            this.invalidateDelServerCache(serverDOList, serverKey2SshExecClient);
        });

        // 缓存sftp client
        poolWorker.getExecutorPool().execute(() -> {
            this.activeNewServerCache(serverDOList, serverKey2SshSftpClient, client -> {
                try {
                    this.uploadScript2Server(client, Constant.LINUX_API_FILE, null);
                } catch (Exception e) {
                    log.error("upload script: {} error. errMsg: {}", Constant.LINUX_API_FILE, e.getMessage());
                }
            });
            this.invalidateDelServerCache(serverDOList, serverKey2SshSftpClient);
        });

        // master需要缓存所有服务器
        masterPoolWorker.getExecutorPool().execute(this::activeMetricClient);

    }

    /**
     * master需要缓存所有服务器
     */
    public void activeMetricClient() {
        if (!masterPoolWorker.isMaster()) {
            return;
        }
        List<HokageServerDO> serverList = serverDao.selectAll();
        if (CollectionUtils.isEmpty(serverList)) {
            return;
        }

        log.info("all server size: {}", serverList.size());

        masterPoolWorker.getExecutorPool().execute(() -> {
            this.activeNewServerCache(serverList, server2MetricClient, client -> {
                try {
                    this.uploadScript2Server(client, Constant.LINUX_REPORT_FILE, specifyMaster);
                    // TODO: 执行一下脚本
                } catch (Exception e) {
                    log.error("upload script: {} error. errMsg: {}", Constant.LINUX_REPORT_FILE, e.getMessage());
                }
            });
            this.invalidateDelServerCache(serverList, server2MetricClient);
        });
    }

    private final BiFunction<SshClient, String, String> specifyMaster = (client, script) -> {
      if (StringUtils.isBlank(script)) {
          return StringUtils.EMPTY;
      }
      long id = client.getSshContext().getId();
        HokageServerReportInfoHandlerDO handlerDO = reportHandlerDao.selectById(Constant.MASTER_REPORT_ID);
        String ip = handlerDO.getHandlerIp();
        int port = handlerDO.getHandlerPort();

        return script.replace("#id", String.valueOf(id))
                .replace("#masterIp", ip)
                .replace("#masterPort", String.valueOf(port));
    };

    /**
     * active new add server ssh client
     * @param serverList server list
     * @param cache server ssh client cache
     * @param consumer consumer, for do some job
     */
    private void activeNewServerCache(List<HokageServerDO> serverList, Cache<String, SshClient> cache, Consumer<SshClient> consumer) {

        if (CollectionUtils.isEmpty(serverList)) {
            return;
        }

        Map<String, HokageServerDO> serverKey2ServerMap = serverList.stream().collect(Collectors.toMap(this::buildKey, Function.identity(), (o1, o2) -> o1));
        Map<String, SshClient> serverKey2ClientMap = cache.asMap();
        // new add server list
        List<String> newServerKeyList = serverKey2ServerMap.keySet().stream()
                .filter(serverKey -> {
                    // new server
                    if (!serverKey2ClientMap.containsKey(serverKey)) {
                        return true;
                    }
                    // server which is loss connection
                    Session session = serverKey2ClientMap.get(serverKey).getSessionIfPresent();
                    return Objects.isNull(session) || !session.isConnected();
                })
                .collect(Collectors.toList());
        // cache new server ssh client
        for (String serverKey : newServerKeyList) {
            poolWorker.getExecutorPool().execute(() -> {
                SshContext context = new SshContext();
                BeanUtils.copyProperties(serverKey2ServerMap.get(serverKey), context);
                try {
                    SshClient client = new SshClient(context);
                    cache.put(serverKey, client);
                    if (Objects.nonNull(consumer)) {
                        consumer.accept(client);
                    }
                    log.info("HokageServerCacheDao.activeCacheRefresh create ssh client: {}", context);
                } catch (Exception e) {
                    log.error("HokageServerCacheDao.activeCacheRefresh create ssh client: {} error. err: {}", context, e.getMessage());
                    cache.put(serverKey, new SshClient().setContext(context));
                }
            });
        }
    }

    /**
     * invalidate deleted server cache
     * @param serverList server list
     * @param cache server ssh client cache
     */
    private void invalidateDelServerCache(List<HokageServerDO> serverList, Cache<String, SshClient> cache) {
        Map<String, HokageServerDO> serverKey2ServerMap = serverList.stream().collect(Collectors.toMap(this::buildKey, Function.identity(), (o1, o2) -> o1));
        Map<String, SshClient> serverKey2ClientMap = cache.asMap();
        // invalid server list
        List<String> invalidServerKeyList = serverKey2ClientMap.keySet().stream()
                .filter(serverKey -> !serverKey2ServerMap.containsKey(serverKey))
                .collect(Collectors.toList());

        // invalidate cache
        for (String serverKey : invalidServerKeyList) {
            try {
                SshClient client = cache.getIfPresent(serverKey);
                if (Objects.nonNull(client) && Objects.nonNull(client.getSessionIfPresent())) {
                    client.getSessionOrCreate().disconnect();
                }
            } catch (Exception ignored) {

            } finally {
                cache.invalidate(serverKey);
                log.info("HokageServerCacheDao.invalidateDelServerCache invalidate ssh client. serverKey: {}", serverKey);
            }
        }
    }

    private void uploadScript2Server(SshClient client, String fileName, BiFunction<SshClient, String, String> function) throws Exception {
        String script = dispatcher.dispatchScript(client);

        if (Objects.nonNull(function)) {
            script = function.apply(client, script);
        }

        ChannelSftp sftp = null;
        InputStream in = null;
        try {
            sftp = (ChannelSftp) client.getSessionOrCreate().openChannel(JSchChannelType.SFTP.getValue());
            sftp.connect();
            // 判断工作目录不存在，若不存在，则创建
            String home = Paths.get(sftp.getHome(), Constant.WORK_HOME).toAbsolutePath().toString();
            try {
                sftp.stat(home);
            } catch (Exception e) {
                sftp.mkdir(home);
            }
            in = new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8));
            String dst = Paths.get(sftp.getHome(), Constant.WORK_HOME).resolve(fileName).toAbsolutePath().toString();
            sftp.put(in, dst, ChannelSftp.OVERWRITE);

            sftp.chmod(511, dst);

            log.info("HokageServerCacheDao.uploadScript2Server success. server: {}", client.getSshContext());
        } catch (Exception e) {
            log.error("HokageServerCacheDao.uploadScript2Server error. server: {}, err: {}", client.getSshContext(), e.getMessage());
        } finally {
            if (Objects.nonNull(sftp) && sftp.isConnected()) {
                sftp.disconnect();
            }
            if (Objects.nonNull(in)) {
                in.close();
            }
        }
    }

    public void invalidateMetricCache() {
        server2MetricClient.invalidateAll();
    }
}
