package com.hokage.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.Constant;
import com.hokage.biz.response.resource.network.InterfaceIpVO;
import com.hokage.biz.service.ReportService;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/07/08 1:05 am
 * @description server report info handler service
 **/
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private static final String ADDR_PREFIX = "addr:";

    private HokageServerDao serverDao;

    @Autowired
    public void setServerDao(HokageServerDao serverDao) {
        this.serverDao = serverDao;
    }

    @Override
    public void ipHandle(List<InterfaceIpVO> interfaceList) {

        interfaceList = interfaceList.stream()
                .filter(inter -> StringUtils.isNotBlank(inter.getIp()) && !StringUtils.contains(inter.getIp(), Constant.LOCAL_HOST))
                .peek(inter -> {
                    if (StringUtils.isBlank(inter.getIp())) {
                        inter.setIp(StringUtils.EMPTY);
                        return;
                    }
                    if (StringUtils.containsIgnoreCase(inter.getIp(), ADDR_PREFIX)) {
                        int index = StringUtils.indexOfIgnoreCase(inter.getIp(), ADDR_PREFIX);
                        String ip = StringUtils.substring(inter.getIp(), index + ADDR_PREFIX.length());
                        inter.setIp(ip);
                    }
                }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(interfaceList)) {
            log.warn("report interface list is empty.");
            return;
        }

        long serverId = interfaceList.get(0).getId();
        HokageServerDO serverDO = serverDao.selectById(serverId);
        if (Objects.isNull(serverDO)) {
            log.error("服务器不存在, server id: " + serverId);
            return;
        }

        // 判断服务器ip是否变化
        boolean isChanged = interfaceList.stream().noneMatch(inter -> StringUtils.equals(inter.getIp(), serverDO.getIp()));
        if (!isChanged) {
            return;
        }

        // 是否可达
        int port = Integer.parseInt(serverDO.getSshPort());
        Optional<InterfaceIpVO> optional = interfaceList.stream().filter(inter -> {
            if (StringUtils.isEmpty(inter.getIp())) {
                return false;
            }
            try {
                return isReachable(inter.getIp(), port);
            } catch (IOException ignored) {
                return false;
            }
        }).findFirst();

        // 上报的ip都不可用？
        if (!optional.isPresent()) {
            log.error("all report ips are unreachable. interface list: " + JSON.toJSONString(interfaceList));
            return;
        }
        // TODO: 消息通知
        serverDO.setIp(optional.get().getIp());
        serverDao.update(serverDO);
    }

    /**
     * check the ssh socket reachable
     * @param ip server ip
     * @param port server port
     * @return true: reachable, false: unreachable
     */
    private boolean isReachable(String ip, int port) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 10 * 1000);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (socket.isConnected()) {
                socket.close();
            }
        }
    }
}
