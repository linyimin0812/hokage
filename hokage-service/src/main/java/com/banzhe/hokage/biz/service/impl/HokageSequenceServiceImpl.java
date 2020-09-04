package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.enums.SequenceErrorCodeEnum;
import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageSequenceDao;
import com.banzhe.hokage.persistence.dataobject.HokageSequenceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/8/30 5:16 下午
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
            throw new RuntimeException(String.format("name: %s 对应的sequence不存在。", name));
        }
        // 更新下一个值
        sequenceDO.setValue(sequenceDO.getValue() + 1);
        Integer result = sequenceDao.update(sequenceDO);
        if (result <= 0) {
            throw new RuntimeException(String.format("name: %s 对应的sequence更新失败。", name));
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
