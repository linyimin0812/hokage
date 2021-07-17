package com.hokage.biz.converter.server;

import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.persistence.dao.HokageSubordinateServerDao;
import com.hokage.persistence.dao.HokageSupervisorServerDao;
import com.hokage.persistence.dao.HokageUserDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.hokage.persistence.dataobject.HokageUserDO;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    private static final ImmutableMap<ConverterTypeEnum, Server2VO> CONVERTER_MAP =
        ImmutableMap.<ConverterTypeEnum, Server2VO>builder()
            .put(ConverterTypeEnum.all, new AllServer2VO())
            .put(ConverterTypeEnum.supervisor, new SupervisorServerDO2VO())
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

        // TODO: retrieve server status from ssh

        // number of users of the server
        List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.listByServerIds(
                Collections.singletonList(serverDO.getId())
        );
        serverVO.setUserNum(subordinateServerDOList.size());

        return serverVO;
    }

}
