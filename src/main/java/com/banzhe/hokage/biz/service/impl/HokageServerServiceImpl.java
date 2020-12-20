package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.converter.ConverterTypeEnum;
import com.banzhe.hokage.biz.converter.ServerConverter;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.biz.service.HokageServerService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Autowired
    public void setHokageServerDao(HokageServerDao hokageServerDao) {
        this.hokageServerDao = hokageServerDao;
    }

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
                .map(serverDO -> ServerConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());
        response.success(serverVOList);
        return response;
    }

    @Override
    public ServiceResponse<List<HokageServerDO>> selectByIds(List<Long> ids) {
        return null;
    }

    @Override
    public ServiceResponse<HokageServerDO> selectById(Long id) {
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
