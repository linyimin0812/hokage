package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageServerMetricDao;
import com.hokage.persistence.dataobject.HokageServerMetricDO;
import com.hokage.persistence.mapper.HokageServerMetricMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/22 12:19 am
 * @description server metric dao interface implementation
 **/
@Repository
public class HokageServerMetricDaoImpl implements HokageServerMetricDao {

    private HokageServerMetricMapper serverMetricMapper;

    @Autowired
    public void setServerMetricMapper(HokageServerMetricMapper serverMetricMapper) {
        this.serverMetricMapper = serverMetricMapper;
    }

    @Override
    public Long insert(HokageServerMetricDO serverMetricDO) {
        return serverMetricMapper.insert(serverMetricDO);
    }

    @Override
    public Long batInsert(List<HokageServerMetricDO> serverMetricDOList) {
        return serverMetricMapper.batInsert(serverMetricDOList);
    }

    @Override
    public List<HokageServerMetricDO> queryByTimeInterval(Long start, Long end) {
        List<HokageServerMetricDO> serverMetricDOList = serverMetricMapper.queryByTimeInterval(start, end);
        if (CollectionUtils.isEmpty(serverMetricDOList)) {
            return Collections.emptyList();
        }
        return serverMetricDOList;
    }

    @Override
    public List<HokageServerMetricDO> queryByTimeIntervalAndType(Long start, Long end, Integer type) {
        List<HokageServerMetricDO> serverMetricDOList = serverMetricMapper.queryByTimeIntervalAndType(start, end, type);
        if (CollectionUtils.isEmpty(serverMetricDOList)) {
            return Collections.emptyList();
        }
        return serverMetricDOList;
    }
}
