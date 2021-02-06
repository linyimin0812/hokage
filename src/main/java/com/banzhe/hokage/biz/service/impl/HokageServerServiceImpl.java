package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.converter.ConverterTypeEnum;
import com.banzhe.hokage.biz.converter.ServerDOConverter;
import com.banzhe.hokage.biz.converter.ServerFormConverter;
import com.banzhe.hokage.biz.enums.UserRoleEnum;
import com.banzhe.hokage.biz.form.server.ServerSearchForm;
import com.banzhe.hokage.biz.request.AllServerQuery;
import com.banzhe.hokage.biz.request.SubordinateServerQuery;
import com.banzhe.hokage.biz.request.SupervisorServerQuery;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.biz.service.HokageServerService;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author linyimin
 * @date 2020/10/31 1:20 am
 * @email linyimin520812@gmail.com
 * @description hokage server service implementation
 */
@Service
public class HokageServerServiceImpl implements HokageServerService {

    private HokageServerDao hokageServerDao;
    private HokageUserService userService;

    @Autowired
    public void setHokageServerDao(HokageServerDao hokageServerDao) {
        this.hokageServerDao = hokageServerDao;
    }

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    private final ImmutableMap<Integer, Function<ServerSearchForm, List<HokageServerDO>>> SERVER_QUERY_MAP =
            ImmutableMap.<Integer, Function<ServerSearchForm, List<HokageServerDO>>>builder()
                    .put(UserRoleEnum.super_operator.getValue(), (form -> {
                        AllServerQuery query = ServerFormConverter.converterToAll(form);
                        return hokageServerDao.selectByQuery(query);

                    }))
                    .put(UserRoleEnum.supervisor.getValue(), (form -> {
                        SupervisorServerQuery query = ServerFormConverter.converterToSupervisor(form);
                        return hokageServerDao.selectByQuery(query);
                    }))
                    .put(UserRoleEnum.subordinate.getValue(), (form -> {
                        SubordinateServerQuery query = ServerFormConverter.converterToSubordinate(form);
                        return hokageServerDao.selectByQuery(query);
                    }))
                    .build();

    @Override
    public ServiceResponse<Long> insert(HokageServerDO serverDO) {
        return null;
    }

    @Override
    public ServiceResponse<Long> update(HokageServerDO serverDO) {
        return null;
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> selectAll() {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerDO> serverDOList = hokageServerDao.selectAll();
        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());
        response.success(serverVOList);
        return response;
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> listServer(ServerSearchForm form) {

        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();

        Long operateId = form.getOperateId();
        // retrieve operate role
        ServiceResponse<Integer> roleResponse = userService.getRoleByUserId(operateId);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return response.fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        Function<ServerSearchForm, List<HokageServerDO>> queryFunction = SERVER_QUERY_MAP.get(roleResponse.getData());

        if (Objects.isNull(queryFunction)) {
            return response.fail("A-XXX", "search server error, not support search type.");
        }
        List<HokageServerDO> serverDOList = queryFunction.apply(form);

        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        response.success(serverVOList);

        return response;

    }



    @Override
    public ServiceResponse<List<HokageServerDO>> selectByIds(List<Long> ids) {
        return null;
    }

    @Override
    public ServiceResponse<List<HokageServerDO>> selectByType(String type) {
        return null;
    }

    @Override
    public ServiceResponse<List<HokageServerDO>> selectByGroup(String group) {
        return null;
    }
}
