package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageServerReportInfoHandlerDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author yiminlin
 * @date 2021/07/08 9:49 am
 * @description hoakge server report handler dao interface
 **/
public interface HokageServerReportHandlerDao {
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
