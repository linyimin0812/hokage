package com.hokage.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.response.resource.metric.MetricMetaVO;
import com.hokage.biz.response.resource.metric.MetricVO;
import com.hokage.biz.service.HokageServerMetricService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageServerMetricDao;
import com.hokage.persistence.dataobject.HokageServerMetricDO;
import com.hokage.ssh.enums.MetricTypeEnum;
import com.hokage.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/23 2:02 am
 * @description server metric service interface implementation
 **/
@Service
public class HokageServerMetricServiceImpl implements HokageServerMetricService {

    private HokageServerMetricDao metricDao;

    @Autowired
    public void setMetricDao(HokageServerMetricDao metricDao) {
        this.metricDao = metricDao;
    }

    @Override
    public ServiceResponse<MetricVO> acquireMetric(String server, Long start, Long end) {

        ServiceResponse<MetricVO> response = new ServiceResponse<>();

        List<HokageServerMetricDO> metricDOList = metricDao.queryByTimeInterval(server, start, end);

        if (CollectionUtils.isEmpty(metricDOList)) {
            return response.success();
        }

        Map<Integer, List<HokageServerMetricDO>> metricMap = metricDOList.stream().collect(Collectors.groupingBy(HokageServerMetricDO::getType));

        Map<String, MetricMetaVO> map = new HashMap<>(8);

        Arrays.stream(MetricTypeEnum.values()).forEach(type -> {
            MetricMetaVO metricMetaVO = new MetricMetaVO();
            Map<String, List<HokageServerMetricDO>> metricTypeMap = metricMap.get(type.getValue()).stream().collect(Collectors.groupingBy(HokageServerMetricDO::getName));
            List<String> xAxis = new ArrayList<>(metricTypeMap.values()).get(0).stream()
                    .sorted((o1, o2) -> o1.getTimestamp() - o2.getTimestamp() > 0 ? 1 : -1)
                    .map(metricDO -> TimeUtil.format(metricDO.getTimestamp(), TimeUtil.METRIC_FORMAT)).collect(Collectors.toList());
            List<MetricMetaVO.SeriesVO> series = new ArrayList<>();

            metricTypeMap.forEach((name, metrics) -> {
                MetricMetaVO.SeriesVO seriesVO = new MetricMetaVO.SeriesVO();
                seriesVO.setName(name);
                List<Double> dataList = metrics.stream().sorted((o1, o2) -> o1.getTimestamp() - o2.getTimestamp() > 0 ? 1 : -1)
                        .map(HokageServerMetricDO::getValue).collect(Collectors.toList());
                seriesVO.setData(dataList);
                series.add(seriesVO);
            });
            metricMetaVO.setSeries(series).setTimeList(xAxis);
            map.put(type.getField(), metricMetaVO);
        });
        return response.success(JSON.parseObject(JSON.toJSONString(map), MetricVO.class));
    }

}
