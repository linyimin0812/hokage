package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageServerMetricDO;
import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/22 12:16 am
 * @description server metric dao interface
 **/
public interface HokageServerMetricDao {
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
    Long batInsert(List<HokageServerMetricDO> serverMetricDOList);

    /**
     * query server metric between start and end
     * @param server server identify
     * @param start start time
     * @param end end time
     * @return server metric list between start and end
     */
    List<HokageServerMetricDO> queryByTimeInterval(String server, Long start, Long end);

    /**
     * query server metric between start and end
     * @param start start time
     * @param end end time
     * @return server metric list between start and end
     */
    List<HokageServerMetricDO> queryByTimeInterval(Long start, Long end);

    /**
     * query server metric between start and end and type
     * @param server server identify
     * @param start start time
     * @param end end time
     * @param type metric type
     * @return server metric list between start and end and type
     */
    List<HokageServerMetricDO> queryByTimeIntervalAndType(String server, Long start, Long end, Integer type);
}
