package com.hokage.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.hokage.infra.worker.ScheduledThreadPoolWorker;
import com.hokage.infra.worker.ThreadPoolWorker;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.context.SshContext;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private Cache<String, SshClient> serverKey2SshClient;
    private HokageServerDao serverDao;
    private ScheduledThreadPoolWorker scheduledWorker;
    private ThreadPoolWorker poolWorker;

    @Autowired
    public void setServerDao(HokageServerDao serverDao) {
        this.serverDao = serverDao;
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
        serverKey2SshClient = buildDefaultLocalCache();
        scheduledWorker.getScheduledService().scheduleAtFixedRate(() -> {
            try {
                this.activeCacheRefresh();
            } catch (Exception e) {
                log.error("HokageServerCacheDao.activeCacheRefresh error. err: " + JSON.toJSONString(e));
            }
        }, 0, cacheRefreshIntervalSecond, TimeUnit.SECONDS);
    }

    public Optional<SshClient> get(String key) {
        SshClient client = serverKey2SshClient.getIfPresent(key);
        if (Objects.nonNull(client)) {
            return Optional.of(client);
        }
        return Optional.empty();
    }

    public void put(String key, SshClient sshClient) {
        if (Objects.isNull(sshClient)) {
            throw new RuntimeException("cache value can't be null");
        }
        serverKey2SshClient.put(key, sshClient);
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
        Map<String, SshClient> serverKey2SshClientMap = serverKey2SshClient.asMap();
        Map<String, HokageServerDO> serverKey2ServerMap = serverDOList.stream().collect(Collectors.toMap(this::buildKey, Function.identity(), (o1, o2) -> o1));

        activeNewServerCache(serverKey2ServerMap, serverKey2SshClientMap);
        invalidateDelServerCache(serverKey2ServerMap, serverKey2SshClientMap);
    }

    /**
     * active new add server ssh client
     * @param serverKey2ServerMap server object map
     * @param serverKey2SshClientMap server ssh clients have cached map
     * @throws Exception
     */
    private void activeNewServerCache(Map<String, HokageServerDO> serverKey2ServerMap, Map<String, SshClient> serverKey2SshClientMap) throws Exception {
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
                SshClient client = new SshClient().setContext(context);
                try {
                    client = new SshClient(context);
                    context.setPasswd(null);
                    log.info("HokageServerCacheDao.activeCacheRefresh create ssh client: {}", context);
                } catch (Exception e) {
                    context.setPasswd(null);
                    log.error("HokageServerCacheDao.activeCacheRefresh create ssh client: {} error. err: {}", context, e.getMessage());
                }
                serverKey2SshClient.put(serverKey, client);
            });
        }
    }

    /**
     * invalidate deleted server cache
     * @param serverKey2ServerMap server object map
     * @param serverKey2SshClientMap server ssh clients have cached map
     */
    private void invalidateDelServerCache(Map<String, HokageServerDO> serverKey2ServerMap, Map<String, SshClient> serverKey2SshClientMap) {
        // delete server list
        serverKey2SshClientMap.keySet().stream()
                .filter(serverKey -> !serverKey2ServerMap.containsKey(serverKey))
                .forEach(serverKey -> {
                    try {
                        SshClient client = serverKey2SshClient.getIfPresent(serverKey);
                        if (Objects.nonNull(client) && Objects.nonNull(client.getSessionIfPresent())) {
                            client.getSessionOrCreate().disconnect();
                        }
                    } catch (Exception ignored) {

                    } finally {
                        serverKey2SshClient.invalidate(serverKey);
                        log.info("HokageServerCacheDao.activeCacheRefresh create ssh client. serverKey: {}", serverKey);
                    }
                });
    }
}
