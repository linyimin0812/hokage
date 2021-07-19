package com.hokage.biz.converter.server;

import com.hokage.biz.enums.server.ServerStatusEnum;
import com.hokage.biz.request.command.ServerParam;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dao.HokageSubordinateServerDao;
import com.hokage.persistence.dao.HokageSupervisorServerDao;
import com.hokage.persistence.dao.HokageUserDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.hokage.persistence.dataobject.HokageUserDO;
import com.google.common.collect.ImmutableMap;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.handler.ServerCommandHandler;
import com.jcraft.jsch.Session;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author linyimin
 * @date 2020/12/19 21:21
 * @email linyimin520812@gmail.com
 * @description conversion between server DO and VO
 */
@Component
public class ServerDOConverter {

    private static HokageSubordinateServerDao subordinateServerDao;
    private static HokageSupervisorServerDao supervisorServerDao;
    private static HokageUserDao hokageUserDao;
    private static HokageServerCacheDao serverCacheDao;
    private static HokageServerDao serverDao;
    private static ServerCommandHandler<ServerParam> serverCommandHandler;

    private static final ImmutableMap<ConverterTypeEnum, Server2VO> CONVERTER_MAP =
        ImmutableMap.<ConverterTypeEnum, Server2VO>builder()
            .put(ConverterTypeEnum.all, new AllServer2VO())
            .put(ConverterTypeEnum.supervisor, new SupervisorServerDO2VO())
            .put(ConverterTypeEnum.subordinate, new SubordinateServer2VO())
            .build();

    @Autowired
    public void setSubordinateServerDao(HokageSubordinateServerDao subordinateServerDao) {
        ServerDOConverter.subordinateServerDao = subordinateServerDao;
    }

    @Autowired
    public void setSupervisorServerDao(HokageSupervisorServerDao supervisorServerDao) {
        ServerDOConverter.supervisorServerDao = supervisorServerDao;
    }

    @Autowired
    public void setHokageUserDao(HokageUserDao hokageUserDao) {
        ServerDOConverter.hokageUserDao = hokageUserDao;
    }

    @Autowired
    public void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        ServerDOConverter.serverCacheDao = serverCacheDao;
    }

    @Autowired
    public void setServerCommandHandler(ServerCommandHandler<ServerParam> serverCommandHandler) {
        ServerDOConverter.serverCommandHandler = serverCommandHandler;
    }

    @Autowired
    public void setServerDao(HokageServerDao serverDao) {
        ServerDOConverter.serverDao = serverDao;
    }

    public static HokageServerVO converter2VO(HokageServerDO serverDO, ConverterTypeEnum type) {
        Server2VO serverDo2VO = CONVERTER_MAP.get(type);
        return serverDo2VO.converter(serverDO);
    }

    private interface Server2VO {
        /**
         * serverDO to serverVO
         * @param serverDO server DO
         * @return HokageServerVO
         */
        HokageServerVO converter(HokageServerDO serverDO);
    }

    private static class AllServer2VO implements Server2VO {
        @Override
        public HokageServerVO converter(HokageServerDO serverDO) {

            // 1. set common property
            HokageServerVO hokageServerVO = preConverter(serverDO);

            // set server supervisors
            List<HokageSupervisorServerDO> supervisorServerDOList = supervisorServerDao.listByServerIds(
                    Collections.singletonList(serverDO.getId())
            );
            List<Long> supervisorIds = supervisorServerDOList.stream()
                    .map(HokageSupervisorServerDO::getSupervisorId)
                    .collect(Collectors.toList());
            List<HokageUserDO> supervisorList = hokageUserDao.listUserByIds(supervisorIds);
            supervisorIds = supervisorList.stream()
                    .map(HokageUserDO::getId)
                    .collect(Collectors.toList());
            List<String> supervisorNameList = supervisorList.stream()
                    .map(HokageUserDO::getUsername)
                    .collect(Collectors.toList());
            hokageServerVO.setSupervisorList(supervisorNameList);
            hokageServerVO.setSupervisorIdList(supervisorIds);

            // number of users of the server
            List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.listByServerIds(
                    Collections.singletonList(serverDO.getId())
            );
            hokageServerVO.setUserNum(subordinateServerDOList.size());

            return hokageServerVO;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    static class SupervisorServerDO2VO implements Server2VO {

        @Override
        public HokageServerVO converter(HokageServerDO serverDO) {

            // 1. set common property
            HokageServerVO serverVO = preConverter(serverDO);

            // number of users of the server
            List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.listByServerIds(
                    Collections.singletonList(serverDO.getId())
            );
            serverVO.setUserNum(subordinateServerDOList.size());

            return serverVO;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    static class SubordinateServer2VO implements Server2VO {

        @Override
        public HokageServerVO converter(HokageServerDO serverDO) {
            return preConverter(serverDO);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private static HokageServerVO preConverter(HokageServerDO serverDO) {

        HokageServerVO serverVO = new HokageServerVO();
        // server information
        BeanUtils.copyProperties(serverDO, serverVO);

        if (StringUtils.isNoneBlank(serverDO.getServerGroup())) {
            serverVO.setServerGroupList(Arrays.asList(serverDO.getServerGroup().split(",")));
        } else {
            serverVO.setServerGroupList(Collections.emptyList());
        }

        // retrieve server status from ssh
        ServerStatusEnum statusEnum = acquireServerStatus(serverDO.buildKey());
        serverVO.setStatus(statusEnum.getStatus());

        // set hostname
        if (StringUtils.isEmpty(serverDO.getHostname())) {
            serverVO.setHostname(acquireHostname(serverDO));
        }

        return serverVO;
    }

    private static ServerStatusEnum acquireServerStatus(String serverKey) {
        try {
            Optional<SshClient> optional = serverCacheDao.getExecClient(serverKey);
            if (!optional.isPresent()) {
                return ServerStatusEnum.unknown;
            }
            Session session = optional.get().getSessionOrCreate();
            if (session.isConnected()) {
                return ServerStatusEnum.online;
            }
            return ServerStatusEnum.offline;
        } catch (Exception e) {
            return ServerStatusEnum.offline;
        }
    }

    private static String acquireHostname(HokageServerDO serverDO) {
        try {
            Optional<SshClient> optional = serverCacheDao.getExecClient(serverDO.buildKey());
            if (!optional.isPresent()) {
                return StringUtils.EMPTY;
            }
            SshClient client = optional.get();
            ServiceResponse<String> response = serverCommandHandler.hostnameHandler.apply(client, null);
            if (response.getSucceeded()) {
                serverDO.setHostname(response.getData());
                serverDao.update(serverDO);
                return response.getData();
            }
            return StringUtils.EMPTY;
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

}
