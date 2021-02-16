package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageServerApplicationDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author linyimin
 * @date 2021/2/16 15:16
 * @email linyimin520812@gmail.com
 * @description hokage server application mapper interface
 */
@Mapper
@Component
public interface HokageServerApplicationMapper {
    /**
     * insert a new record
     * @param applicationDO
     * @return
     */
    Integer insert(HokageServerApplicationDO applicationDO);

    /**
     * update a record
     * @param applicationDO
     * @return
     */
    Integer update(HokageServerApplicationDO applicationDO);
}
