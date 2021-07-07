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

        Map<String, List<MetricMetaVO>> map = new HashMap<>(8);

        Arrays.stream(MetricTypeEnum.values()).forEach(type -> {
            List<MetricMetaVO> metricMetaVOList = metricMap.get(type.getValue()).stream()
                    .map(metricDO -> {
                        MetricMetaVO metaVO = new MetricMetaVO();
                        metaVO.setCategory(metricDO.getName())
                                .setValue(metricDO.getValue())
                                .setTime(TimeUtil.format(metricDO.getTimestamp(), TimeUtil.METRIC_FORMAT));
                        return metaVO;
                    })
                    .sorted(MetricMetaVO::compareTo)
                    .collect(Collectors.toList());

            map.put(type.getField(), metricMetaVOList);
        });
        return response.success(JSON.parseObject(JSON.toJSONString(map), MetricVO.class));
    }

}
