package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageServerReportInfoHandlerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/07/08 9:31 am
 * @description hokage server report info handler
 **/
@Mapper
@Component
public interface HokageServerReportInfoHandlerMapper {
    /**
     * insert a new handler record
     * @param handlerDO report info handler data object
     * @return rows affected
     */
    Long insert(HokageServerReportInfoHandlerDO handlerDO);

    /**
     * update a handler record
     * @param handlerDO report info handler data object
     * @return rows affected
     */
    Long update(HokageServerReportInfoHandlerDO handlerDO);

    /**
     * select a record by primary id
     * @param id record primary id
     * @return record which meets the criteria
     */
    HokageServerReportInfoHandlerDO selectById(@Param("id") Long id);
}
