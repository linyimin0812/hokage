package com.banzhe.hokage.biz.converter;

import com.banzhe.hokage.biz.enums.OperationTypeEnum;
import com.banzhe.hokage.biz.form.user.HokageUserLoginForm;
import com.banzhe.hokage.biz.form.user.HokageUserRegisterForm;
import com.banzhe.hokage.biz.response.HokageOperation;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.biz.response.user.HokageUserVO;
import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dao.HokageSubordinateServerDao;
import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
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
 * @date 2020/8/30 3:14 pm
 * @email linyimin520812@gmail.com
 * @description conversion between user VO and DO
 */
@Component
public class UserConverter {

    private static HokageSupervisorServerDao supervisorServerDao;
    private static HokageSubordinateServerDao subordinateServerDao;
    private static HokageServerDao hokageServerDao;

    private static final ImmutableMap<ConverterTypeEnum, UserDO2VO> CONVERTER_MAP = ImmutableMap.<ConverterTypeEnum, UserDO2VO>builder()
        .put(ConverterTypeEnum.supervisor, new SupervisorUserDO2VO())
        .put(ConverterTypeEnum.subordinate, new SubordinateUserDO2VO())
        .build();

    @Autowired
    public void setSupervisorServerDao(HokageSupervisorServerDao hokageSupervisorServerDao) {
        UserConverter.supervisorServerDao = hokageSupervisorServerDao;
    }

    @Autowired
    public void setSubordinateServerDao(HokageSubordinateServerDao hokageSubordinateServerDao) {
        UserConverter.subordinateServerDao = hokageSubordinateServerDao;
    }

    @Autowired
    public void setHokageServerDao(HokageServerDao hokageServerDao) {
        UserConverter.hokageServerDao = hokageServerDao;
    }

    public static HokageUserDO registerFormToDO(HokageUserRegisterForm userRegisterForm) {
        HokageUserDO userDO = new HokageUserDO();
        BeanUtils.copyProperties(userRegisterForm, userDO);
        return userDO;
    }

    public static HokageUserRegisterForm DOToRegisterForm(HokageUserDO userDO) {
        HokageUserRegisterForm form = new HokageUserRegisterForm();
        BeanUtils.copyProperties(userDO, form);
        return form;
    }

    public static HokageUserDO loginFormToDO(HokageUserLoginForm loginForm) {
        HokageUserDO userDO = new HokageUserDO();
        BeanUtils.copyProperties(loginForm, userDO);
        return userDO;
    }

    public static HokageUserLoginForm DOToLoginForm(HokageUserDO userDO) {
        HokageUserLoginForm loginForm = new HokageUserLoginForm();
        BeanUtils.copyProperties(loginForm, userDO);
        return loginForm;
    }

    public static HokageUserVO converter(HokageUserDO hokageUserDO, ConverterTypeEnum type) {
        UserDO2VO userDO2VO = CONVERTER_MAP.get(type);
        return userDO2VO.converter(hokageUserDO);
    }


    interface UserDO2VO {
        /**
         * userDO to userVO
         * @param hokageUserDO hokage user data object
         * @return
         */
        HokageUserVO converter(HokageUserDO hokageUserDO);
    }

    private static class SupervisorUserDO2VO implements UserDO2VO {

        @Override
        public HokageUserVO converter(HokageUserDO hokageUserDO) {

            HokageUserVO hokageUserVO = preConverter(hokageUserDO);
            // server information which managed by the supervisor
            List<Long> serverIds = supervisorServerDao.listByServerIds(Arrays.asList(hokageUserDO.getId()))
                    .stream()
                    .map(HokageSupervisorServerDO::getServerId)
                    .collect(Collectors.toList());

            List<HokageServerVO> serverVOList = hokageServerDao.selectByIds(serverIds).stream().map(serverDO -> {

                HokageServerVO serverVO = ServerConverter.converterDO2VO(serverDO, ConverterTypeEnum.supervisor);

                // supervisor info
                serverVO.setSupervisor(Collections.singletonList(hokageUserDO.getUsername()));
                serverVO.setSupervisorId(Collections.singletonList(hokageUserDO.getId()));

                return serverVO;

            }).collect(Collectors.toList());

            List<String> serverLabels = serverVOList.stream()
                    .flatMap(serverVO -> serverVO.getLabels().stream())
                    .distinct()
                    .collect(Collectors.toList());

            hokageUserVO.setServerLabel(serverLabels);
            hokageUserVO.setServerNum(serverVOList.size());
            hokageUserVO.setServerVOList(serverVOList);

            // action info
            List<HokageOperation> operations = Arrays.asList(
                new HokageOperation(
                        OperationTypeEnum.link.name(),
                        "view",
                        "/user/supervisor/view"),
                new HokageOperation(
                        OperationTypeEnum.modal.name(),
                        "addServer",
                        "/supervisor/server/add"),
                new HokageOperation(
                        OperationTypeEnum.confirm.name(),
                        "recycleServer",
                        "/supervisor/server/recycle")
            );

            hokageUserVO.setOperationList(operations);
            return hokageUserVO;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private static class SubordinateUserDO2VO implements UserDO2VO {

        @Override
        public HokageUserVO converter(HokageUserDO hokageUserDO) {

            HokageUserVO hokageUserVO = preConverter(hokageUserDO);

            // server information which managed by the supervisor
            List<Long> serverIds = subordinateServerDao.listByServerIds(Collections.singletonList(hokageUserDO.getId())).stream()
                    .map(HokageSubordinateServerDO::getServerId).collect(Collectors.toList());

            List<HokageServerVO> serverVOList = hokageServerDao.selectByIds(serverIds).stream().map(serverDO -> {

                HokageServerVO serverVO = ServerConverter.converterDO2VO(serverDO, ConverterTypeEnum.supervisor);

                // supervisor info
                serverVO.setSubordinate(Collections.singletonList(hokageUserDO.getUsername()));
                serverVO.setSubordinateId(Collections.singletonList(hokageUserDO.getId()));

                // number of users of the server
                List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.listByServerIds(
                    Collections.singletonList(serverDO.getId())
                );
                serverVO.setUserNum(subordinateServerDOList.size());

                return serverVO;
            }).collect(Collectors.toList());

            List<String> serverLabels = serverVOList.stream()
                    .flatMap(serverVO -> serverVO.getLabels().stream())
                    .distinct()
                    .collect(Collectors.toList());

            hokageUserVO.setServerLabel(serverLabels);
            hokageUserVO.setServerNum(serverVOList.size());
            hokageUserVO.setServerVOList(serverVOList);

            // action info
            List<HokageOperation> operations = Arrays.asList(
                new HokageOperation(
                        OperationTypeEnum.link.name(),
                        "view",
                        "/user/subordinate/view"),
                new HokageOperation(
                        OperationTypeEnum.modal.name(),
                        "addServer",
                        "/subordinate/server/add"),
                new HokageOperation(
                        OperationTypeEnum.confirm.name(),
                        "deleteSubordinate",
                        "/supervisor/subordinate/delete")
            );

            hokageUserVO.setOperationList(operations);

            return hokageUserVO;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private static HokageUserVO preConverter(HokageUserDO hokageUserDO) {
        HokageUserVO hokageUserVO = new HokageUserVO();

        HokageUserVO userVO = new HokageUserVO();

        // subordinate info
        BeanUtils.copyProperties(hokageUserDO, userVO);

        return hokageUserVO;
    }
}
