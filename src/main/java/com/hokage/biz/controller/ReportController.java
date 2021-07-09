package com.hokage.biz.controller;

import com.hokage.biz.response.resource.network.InterfaceIpVO;
import com.hokage.biz.service.ReportService;
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
@RestController
public class ReportController {
    private ReportService reportService;

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = "/report/ip", method = RequestMethod.POST)
    public void ipHandle(@RequestBody List<InterfaceIpVO> interfaceList) {
        // TODO: 服务器上报的ip为空
        if (CollectionUtils.isEmpty(interfaceList)) {
            return;
        }
        reportService.ipHandle(interfaceList);
    }
}
