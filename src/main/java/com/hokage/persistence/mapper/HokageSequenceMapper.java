package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageSequenceDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author linyimin
 * @date 2020/8/30 4:38 pm
 * @email linyimin520812@gmail.com
 * @description mysql table primary key
 */
@Mapper
@Component
public interface HokageSequenceMapper {
    /**
     * insert a new record
     * @param sequenceDO
     * @return
     */
    Integer insert(HokageSequenceDO sequenceDO);

    
    /**
     * update sequence id
     * @param sequenceDO
     * @return
     */
    Integer update(HokageSequenceDO sequenceDO);

    /**
     * retrieve sequence based-on sequence name
     * @param name
     * @return
     */
    HokageSequenceDO getSequenceByName(String name);
}
