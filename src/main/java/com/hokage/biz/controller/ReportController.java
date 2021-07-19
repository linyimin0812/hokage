package com.hokage.biz.controller;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.response.resource.network.InterfaceIpVO;
import com.hokage.biz.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/07/08 1:18 am
 * @description server report controller
 **/
@Slf4j
@RestController
public class ReportController {
    private ReportService reportService;

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = "/report/ip", method = RequestMethod.POST)
    public void ipHandle(@RequestBody List<InterfaceIpVO> interfaceList) {
        log.info("receive report info: " + JSON.toJSONString(interfaceList));
        if (CollectionUtils.isEmpty(interfaceList)) {
            return;
        }
        reportService.ipHandle(interfaceList);
    }
}
