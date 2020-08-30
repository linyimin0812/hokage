package com.banzhe.hokage.biz.service;

import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dataobject.HokageSequenceDO;

/**
 * @author linyimin
 * @date 2020/8/30 5:10 下午
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSequenceService {
    /**
     * 根据名称获取下一个序列值
     * @param name
     * @return
     */
    ServiceResponse<Long> nextValue(String name);

    /**
     * 插入一条序列值
     * @param sequenceDO
     * @return
     */
    ServiceResponse<Boolean> insert(HokageSequenceDO sequenceDO);

}
