package com.banzhe.hokage.persistence.dao;

import com.banzhe.hokage.persistence.dataobject.HokageSequenceDO;

/**
 * @author linyimin
 * @date 2020/8/30 4:29 下午
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSequenceDao {
    /**
     * 插入一条新纪录
     * @param sequenceDO
     * @return
     */
    Integer insert(HokageSequenceDO sequenceDO);

    /**
     * 更新序列值
     * @param sequenceDO
     * @return
     */
    Integer update(HokageSequenceDO sequenceDO);

    /**
     * 根据序列名称获取序列值
     * @param name
     * @return
     */
    HokageSequenceDO getSequenceByName(String name);
}
