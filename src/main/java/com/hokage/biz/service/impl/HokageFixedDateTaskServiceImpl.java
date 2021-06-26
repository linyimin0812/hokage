package com.hokage.biz.service.impl;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.service.HokageFixedDateTaskService;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageFixedDateTaskDao;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yiminlin
 * @date 2021/06/27 4:37 am
 * @description fixed date task service implementation
 **/
@Service
public class HokageFixedDateTaskServiceImpl implements HokageFixedDateTaskService {

    private HokageFixedDateTaskDao fixedDateTaskDao;
    private HokageSequenceService sequenceService;

    @Autowired
    public void setFixedDateTaskDao(HokageFixedDateTaskDao fixedDateTaskDao) {
        this.fixedDateTaskDao = fixedDateTaskDao;
    }

    @Autowired
    public void setSequenceService(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public ServiceResponse<Long> upsert(HokageFixedDateTaskDO fixedDateTaskDO) {
        ServiceResponse<Long> response = new ServiceResponse<>();
        if (Objects.isNull(fixedDateTaskDO.getId()) || fixedDateTaskDO.getId() == 0) {
            ServiceResponse<Long> sequenceResult = sequenceService.nextValue(SequenceNameEnum.hokage_fixed_date_task.name());
            fixedDateTaskDO.setId(sequenceResult.getData());
            Long result = fixedDateTaskDao.insert(fixedDateTaskDO);
            return result > 0 ? response.success(sequenceResult.getData()) : response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "insert fixed date error");
        }
        Long result = fixedDateTaskDao.update(fixedDateTaskDO);
        return result > 0 ? response.success(result) : response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "update fixed date error");
    }

    @Override
    public ServiceResponse<HokageFixedDateTaskDO> findById(Long id) {
        ServiceResponse<HokageFixedDateTaskDO> response = new ServiceResponse<>();
        return response.success(fixedDateTaskDao.findById(id));
    }

    @Override
    public ServiceResponse<List<HokageFixedDateTaskDO>> findByName(String name) {
        ServiceResponse<List<HokageFixedDateTaskDO>> response = new ServiceResponse<>();

        return response.success(fixedDateTaskDao.findByName(name));
    }

    @Override
    public ServiceResponse<List<HokageFixedDateTaskDO>> findByType(Integer type) {
        ServiceResponse<List<HokageFixedDateTaskDO>> response = new ServiceResponse<>();

        return response.success(fixedDateTaskDao.findByType(type));
    }

    @Override
    public ServiceResponse<List<HokageFixedDateTaskDO>> findAll(HokageFixedDateTaskDO fixedDateTaskDO) {
        ServiceResponse<List<HokageFixedDateTaskDO>> response = new ServiceResponse<>();

        return response.success(fixedDateTaskDao.findAll(fixedDateTaskDO));
    }
}
