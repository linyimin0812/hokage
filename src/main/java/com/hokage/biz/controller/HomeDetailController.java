package com.hokage.biz.controller;

import com.hokage.biz.response.home.HomeDetailVO;
import com.hokage.biz.response.home.HomeMetricVO;
import com.hokage.biz.response.resource.metric.MetricVO;
import com.hokage.biz.service.HokageServerMetricService;
import com.hokage.biz.service.HomeService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yiminlin
 * @date 2021/07/19 9:17 pm
 * @description home detail controller
 **/
@Slf4j
@RestController
public class HomeDetailController extends BaseController {

    private HomeService homeService;
    private HokageServerMetricService metricService;

    @Autowired
    public void setHomeService(HomeService homeService) {
        this.homeService = homeService;
    }

    @Autowired
    public void setMetricService(HokageServerMetricService metricService) {
        this.metricService = metricService;
    }

    @RequestMapping(value = "/home/detail", method = RequestMethod.GET)
    public ResultVO<HomeDetailVO> homeDetail() {
        ServiceResponse<HomeDetailVO> response = homeService.homeDetail();
        return response(response);
    }

    @RequestMapping(value = "/home/metric", method = RequestMethod.GET)
    public ResultVO<HomeMetricVO> acquireSystemInfo() {

        ServiceResponse<String> response = homeService.acquireMostBusyServerIp();
        if (!response.getSucceeded() || StringUtils.isEmpty(response.getData())) {
            return success(null);
        }
        String ip = response.getData();
        long end = System.currentTimeMillis();
        long start = end - 30 * 60 * 1000;
        ServiceResponse<MetricVO> result = metricService.acquireMetric(ip, start, end);

        if (result.getSucceeded()) {
            return success(new HomeMetricVO(ip, result.getData()));
        }

        return fail(result.getCode(), result.getMsg());
    }
}
