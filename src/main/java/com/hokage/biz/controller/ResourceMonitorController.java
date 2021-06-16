package com.hokage.biz.controller;

import com.hokage.biz.form.monitor.MonitorOperateForm;
import com.hokage.biz.response.resource.BasicInfoVO;
import com.hokage.biz.service.HokageMonitorService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linyimin
 * @date 2020/8/23 01:52
 * @email linyimin520812@gmail.com
 * @description
 */
@RestController
public class ResourceMonitorController extends BaseController {
    private HokageMonitorService monitorService;

    @Autowired
    public void setMonitorService(HokageMonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @RequestMapping(value = "/server/monitor/basic", method = RequestMethod.POST)
    public ResultVO<BasicInfoVO> acquireBasicInfo(@RequestBody MonitorOperateForm form) {
        String serverKey = form.buildKey();
        ServiceResponse<BasicInfoVO> result = monitorService.acquireBasic(serverKey);

        return response(result);
    }
}
