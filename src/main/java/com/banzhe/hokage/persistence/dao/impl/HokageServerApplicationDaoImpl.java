package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.biz.enums.SequenceNameEnum;
import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageSequenceDao;
import com.banzhe.hokage.persistence.dao.HokageServerApplicationDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerApplicationDO;
import com.banzhe.hokage.persistence.mapper.HokageServerApplicationMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author linyimin
 * @date 2021/2/16 15:57
 * @email linyimin520812@gmail.com
 * @description hokage server application dao implementation
 */
@Repository
public class HokageServerApplicationDaoImpl implements HokageServerApplicationDao {

    private HokageServerApplicationMapper applicationMapper;
    private HokageSequenceService sequenceService;

    @Autowired
    private void setApplicationMapper(HokageServerApplicationMapper applicationMapper) {
        this.applicationMapper = applicationMapper;
    }

    @Autowired
    private void setSequenceService(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public boolean save(HokageServerApplicationDO applicationDO) {

        // insert
        if (ObjectUtils.defaultIfNull(applicationDO.getId(), 0L) == 0) {
            ServiceResponse<Long> serviceResponse = sequenceService.nextValue(SequenceNameEnum.hokage_server_application.name());
            if (!serviceResponse.getSucceeded() || serviceResponse.getData() <= 0) {
                return false;
            }
            applicationDO.setId(serviceResponse.getData());
            return applicationMapper.insert(applicationDO) > 0;
        }

        // update
        return applicationMapper.update(applicationDO) > 0;
    }
}
