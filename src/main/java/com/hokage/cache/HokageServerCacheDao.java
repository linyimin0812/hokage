package com.hokage.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.hokage.biz.Constant;
import com.hokage.infra.worker.ScheduledThreadPoolWorker;
import com.hokage.infra.worker.ThreadPoolWorker;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
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

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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

    @PostConstruct
    public void init() throws Exception {
        serverKey2SshExecClient = buildDefaultLocalCache();
        serverKey2SshSftpClient = buildDefaultLocalCache();
        server2MetricClient = buildDefaultLocalCache();
        scheduledWorker.getScheduledService().scheduleAtFixedRate(() -> {
            try {
                this.activeCacheRefresh();
            } catch (Exception e) {
                log.error("HokageServerCacheDao.activeCacheRefresh error. err: " + JSON.toJSONString(e));
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
     * @throws Exception
     */
    private void activeCacheRefresh() throws Exception {
        List<HokageServerDO> serverDOList = serverDao.selectAll();

        log.info("total server size: {}", serverDOList.size());

        Map<String, SshClient> serverKey2SshClientMap = serverKey2SshExecClient.asMap();
        Map<String, HokageServerDO> serverKey2ServerMap = serverDOList.stream().collect(Collectors.toMap(this::buildKey, Function.identity(), (o1, o2) -> o1));
        activeNewServerCache(serverKey2ServerMap, serverKey2SshClientMap, "serverKey");
        invalidateDelServerCache(serverKey2ServerMap, serverKey2SshClientMap, "serverKey");

        Map<String, SshClient> server2MetricClientMap = server2MetricClient.asMap();
        Map<String, HokageServerDO> server2ServerMap = serverDOList.stream().collect(Collectors.toMap(HokageServerDO::getIp, Function.identity(), (o1, o2) -> o1));
        activeNewServerCache(serverKey2ServerMap, serverKey2SshClientMap, "server");
        invalidateDelServerCache(serverKey2ServerMap, serverKey2SshClientMap, "server");

    }

    /**
     * active new add server ssh client
     * @param serverKey2ServerMap server object map
     * @param serverKey2SshClientMap server ssh clients have cached map
     * @param type: server --> metric client, serverKey --> execClient and sftClient
     * @throws Exception
     */
    private void activeNewServerCache(Map<String, HokageServerDO> serverKey2ServerMap, Map<String, SshClient> serverKey2SshClientMap, String type) throws Exception {
        // new add server list
        List<String> newServerKeyList = serverKey2ServerMap.keySet().stream()
                .filter(serverKey -> {
                    // new server
                    if (!serverKey2SshClientMap.containsKey(serverKey)) {
                        return true;
                    }
                    // server which is loss connection
                    Session session = serverKey2SshClientMap.get(serverKey).getSessionIfPresent();
                    return Objects.isNull(session) || !session.isConnected();
                })
                .collect(Collectors.toList());

        for (String serverKey : newServerKeyList) {
            poolWorker.getExecutorPool().execute(() -> {
                SshContext context = new SshContext();
                BeanUtils.copyProperties(serverKey2ServerMap.get(serverKey), context);
                if (StringUtils.equals(type, "serverKey")) {
                    SshClient execClient = new SshClient().setContext(context);
                    SshClient sftpClient = new SshClient().setContext(context);
                    try {
                        execClient = new SshClient(context);
                        sftpClient = new SshClient(context);
                        uploadScript2Server(sftpClient);
                        log.info("HokageServerCacheDao.activeCacheRefresh create exec and sftp ssh client: {}", context);
                    } catch (Exception e) {
                        log.error("HokageServerCacheDao.activeCacheRefresh create exec and sftp ssh client: {} error. err: {}", context, e.getMessage());
                    }
                    serverKey2SshExecClient.put(serverKey, execClient);
                    serverKey2SshSftpClient.put(serverKey, sftpClient);
                }
                if (StringUtils.equals(type, "server")) {
                    SshClient metricClient = new SshClient().setContext(context);
                    try {
                        metricClient = new SshClient(context);
                        log.info("HokageServerCacheDao.activeCacheRefresh create metric ssh client: {}", context);
                    } catch (Exception e) {
                        log.error("HokageServerCacheDao.activeCacheRefresh create metric ssh client: {} error. err: {}", context, e.getMessage());
                    }
                    server2MetricClient.put(serverKey, metricClient);
                }
            });
        }
    }

    /**
     * invalidate deleted server cache
     * @param serverKey2ServerMap server object map
     * @param serverKey2SshClientMap server ssh clients have cached map
     * @param type: server --> metric client, serverKey --> execClient and sftClient
     */
    private void invalidateDelServerCache(Map<String, HokageServerDO> serverKey2ServerMap, Map<String, SshClient> serverKey2SshClientMap, String type) {
        // delete server list
        serverKey2SshClientMap.keySet().stream()
                .filter(serverKey -> !serverKey2ServerMap.containsKey(serverKey))
                .forEach(serverKey -> {
                    try {
                        if (StringUtils.equals(type, "serverKey")) {
                            SshClient client = serverKey2SshExecClient.getIfPresent(serverKey);
                            if (Objects.nonNull(client) && Objects.nonNull(client.getSessionIfPresent())) {
                                client.getSessionOrCreate().disconnect();
                            }
                            client = serverKey2SshSftpClient.getIfPresent(serverKey);
                            if (Objects.nonNull(client) && Objects.nonNull(client.getSessionIfPresent())) {
                                client.getSessionOrCreate().disconnect();
                            }
                        }

                        if (StringUtils.equals(type, "server")) {
                            SshClient client = server2MetricClient.getIfPresent(serverKey);
                            if (Objects.nonNull(client) && Objects.nonNull(client.getSessionIfPresent())) {
                                client.getSessionOrCreate().disconnect();
                            }
                        }

                    } catch (Exception ignored) {

                    } finally {
                        serverKey2SshExecClient.invalidate(serverKey);
                        serverKey2SshSftpClient.invalidate(serverKey);
                        server2MetricClient.invalidate(serverKey);
                        log.info("HokageServerCacheDao.activeCacheRefresh invalidate ssh client. serverKey: {}", serverKey);
                    }
                });
    }

    private void uploadScript2Server(SshClient client) throws Exception {
        String script = dispatcher.dispatchScript(client);
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
            String dst = Paths.get(sftp.getHome(), Constant.WORK_HOME).resolve(Constant.API_FILE).toAbsolutePath().toString();
            sftp.put(in, dst, ChannelSftp.OVERWRITE);

            log.info("HokageServerCacheDao.uploadScript2Server success. server: {}", client.getSshContext());
        } catch (Exception e) {
            log.info("HokageServerCacheDao.uploadScript2Server error. server: {}, err: {}", client.getSshContext(), e.getMessage());
        } finally {
            if (Objects.nonNull(sftp) && sftp.isConnected()) {
                sftp.disconnect();
            }
            if (Objects.nonNull(in)) {
                in.close();
            }
        }
    }
}
