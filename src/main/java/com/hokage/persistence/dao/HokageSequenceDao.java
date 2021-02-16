package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageSequenceDO;

/**
 * @author linyimin
 * @date 2020/8/30 4:29 pm
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSequenceDao {
    /**
     * insert a new record
     * @param sequenceDO
     * @return
     */
    Integer insert(HokageSequenceDO sequenceDO);

    /**
     * update a record
     * @param sequenceDO
     * @return
     */
    Integer update(HokageSequenceDO sequenceDO);

    /**
     * retrieve sequence id based-on sequence name
     * @param name
     * @return
     */
    HokageSequenceDO getSequenceByName(String name);
}
