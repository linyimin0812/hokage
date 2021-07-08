package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageServerReportHandlerDao;
import com.hokage.persistence.dataobject.HokageServerReportInfoHandlerDO;
import com.hokage.persistence.mapper.HokageServerReportInfoHandlerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author yiminlin
 * @date 2021/07/08 9:50 am
 * @description hokage server report info handler dao implementation
 **/
@Repository
public class HokageServerReportInfoHandlerDaoImpl implements HokageServerReportHandlerDao {

    private HokageServerReportInfoHandlerMapper handlerMapper;

    @Autowired
    public void setHandlerMapper(HokageServerReportInfoHandlerMapper handlerMapper) {
        this.handlerMapper = handlerMapper;
    }

    @Override
    public Long insert(HokageServerReportInfoHandlerDO handlerDO) {
        return handlerMapper.insert(handlerDO);
    }

    @Override
    public Long update(HokageServerReportInfoHandlerDO handlerDO) {
        return handlerMapper.update(handlerDO);
    }

    @Override
    public HokageServerReportInfoHandlerDO selectById(Long id) {
        return handlerMapper.selectById(id);
    }
}
