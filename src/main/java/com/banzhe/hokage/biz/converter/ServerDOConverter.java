package com.banzhe.hokage.biz.converter;

import com.banzhe.hokage.biz.enums.OperationTypeEnum;
import com.banzhe.hokage.biz.response.HokageOperation;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.persistence.dao.HokageSubordinateServerDao;
import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import com.google.common.collect.ImmutableMap;
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

    private static final ImmutableMap<ConverterTypeEnum, ServerDO2VO> CONVERTER_MAP =
        ImmutableMap.<ConverterTypeEnum, ServerDO2VO>builder()
            .put(ConverterTypeEnum.all, new AllServerDO2VO())
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

    public static HokageServerVO converterDO2VO(HokageServerDO serverDO, ConverterTypeEnum type) {
        ServerDO2VO serverDo2VO = CONVERTER_MAP.get(type);
        return serverDo2VO.converter(serverDO);
    }

    private interface ServerDO2VO {
        /**
         * serverDO to serverVO
         * @param serverDO server DO
         * @return HokageServerVO
         */
        HokageServerVO converter(HokageServerDO serverDO);
    }

    private static class AllServerDO2VO implements ServerDO2VO {
        @Override
        public HokageServerVO converter(HokageServerDO serverDO) {

            // 1. set common property
            HokageServerVO hokageServerVO = preConverter(serverDO);

            // 2. set operation list
            List<HokageOperation> operations = Arrays.asList(
                new HokageOperation(
                        OperationTypeEnum.modal.name(),
                        "specifySupervisor",
                        "/server/supervisor/add"),
                new HokageOperation(
                        OperationTypeEnum.confirm.name(),
                        "recycleSupervisor",
                        "/server/supervisor/recycle"),
                new HokageOperation(
                        OperationTypeEnum.confirm.name(),
                        "modifySupervisor",
                        "/server/supervisor/modify")
            );
            hokageServerVO.setOperationList(operations);

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
            hokageServerVO.setSupervisor(supervisorNameList);
            hokageServerVO.setSupervisorId(supervisorIds);

            return hokageServerVO;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    static class SupervisorServerDO2VO implements ServerDO2VO {

        @Override
        public HokageServerVO converter(HokageServerDO serverDO) {

            // 1. set common property
            HokageServerVO serverVO = preConverter(serverDO);

            // 2. set operation list
            List<HokageOperation> operations = Collections.singletonList(
                new HokageOperation(
                        OperationTypeEnum.confirm.name(),
                        "recycle",
                        "/server/recycle")
            );
            serverVO.setOperationList(operations);

            return serverVO;
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
        serverVO.setLabels(Arrays.asList(serverDO.getLabel().split(",")));

        // TODO: retrieve server status from ssh

        // number of users of the server
        List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.listByServerIds(
                Collections.singletonList(serverDO.getId())
        );
        serverVO.setUserNum(subordinateServerDOList.size());

        return serverVO;
    }

}
