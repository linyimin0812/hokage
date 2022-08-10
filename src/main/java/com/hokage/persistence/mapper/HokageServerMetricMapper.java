package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageServerMetricDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/21 11:49 pm
 * @description server metric mapper
 **/
@Mapper
@Component
public interface HokageServerMetricMapper {
    /**
     * insert a new record
     * @param serverMetricDO  server metric
     * @return effected rows
     */
    Long insert(HokageServerMetricDO serverMetricDO);

    /**
     * insert a list server metrics
     * @param serverMetricDOList server metric list
     * @return effected rows
     */
    Long batInsert(@Param("serverMetricDOList") List<HokageServerMetricDO> s);

    /**
     * query server metric between start and end
     * @param server server ip
     * @param start start time
     * @param end end time
     * @return server metric list between start and end
     */
    List<HokageServerMetricDO> queryByTimeInterval(@Param("server") String server, @Param("start") Long start, @Param("end") Long end);

    /**
     * query server metric between start and end and type
     * @param start start time
     * @param end end time
     * @param type metric type
     * @return server metric list between start and end and type
     */
    List<HokageServerMetricDO> queryByTimeIntervalAndType(@Param("server") String server, @Param("start")Long start, @Param("end") Long end, @Param("type") Integer type);

    /**
     * query server metric between start and end
     * @param start start time
     * @param end end time
     * @return server metric list between start and end
     */
    List<HokageServerMetricDO> queryAllByTimeInterval(Long start, Long end);
}
