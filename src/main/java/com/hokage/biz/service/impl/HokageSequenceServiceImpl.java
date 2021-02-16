package com.hokage.biz.service.impl;

import com.hokage.biz.enums.SequenceErrorCodeEnum;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageSequenceDao;
import com.hokage.persistence.dataobject.HokageSequenceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/8/30 5:16 pm
 * @email linyimin520812@gmail.com
 * @description
 */
@Service
public class HokageSequenceServiceImpl implements HokageSequenceService {

    private HokageSequenceDao sequenceDao;

    @Autowired
    public void setSequenceDao(HokageSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    @Override
    public ServiceResponse<Long> nextValue(String name) {
        ServiceResponse<Long> res = new ServiceResponse<>();
        HokageSequenceDO sequenceDO = sequenceDao.getSequenceByName(name);
        if (Objects.isNull(sequenceDO)) {
            throw new RuntimeException(String.format("name: %s of sequence is not exist", name));
        }
        // update next value
        sequenceDO.setValue(sequenceDO.getValue() + 1);
        Integer result = sequenceDao.update(sequenceDO);
        if (result <= 0) {
            throw new RuntimeException(String.format("name: %s of sequence update failed", name));
        }
        return res.success(sequenceDO.getValue());
    }

    @Override
    public ServiceResponse insert(HokageSequenceDO sequenceDO) {

        ServiceResponse res = new ServiceResponse<>();
        Integer result = sequenceDao.insert(sequenceDO);

        if (result > 0) {
            return res.success();
        }
        return res.fail(SequenceErrorCodeEnum.SEQUENCE_INSERT_ERROR.getCode(), SequenceErrorCodeEnum.SEQUENCE_INSERT_ERROR.getMsg()) ;
    }
}
