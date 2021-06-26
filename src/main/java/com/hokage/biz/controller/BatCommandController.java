package com.hokage.biz.controller;

import com.hokage.biz.converter.bat.FixedDateTaskConverter;
import com.hokage.biz.form.bat.HokageFixedDateTaskForm;
import com.hokage.biz.service.HokageFixedDateTaskService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linyimin
 * @date 2020/8/23 1:51
 * @email linyimin520812@gmail.com
 * @description bat command controller
 */
@Slf4j
@RestController
public class BatCommandController extends BaseController {
    private HokageFixedDateTaskService fixedDateTaskService;
    private FixedDateTaskConverter fixedDateTaskConverter;

    @Autowired
    public void setFixedDateTaskService(HokageFixedDateTaskService fixedDateTaskService) {
        this.fixedDateTaskService = fixedDateTaskService;
    }

    @Autowired
    public void setFixedDateTaskConverter(FixedDateTaskConverter fixedDateTaskConverter) {
        this.fixedDateTaskConverter = fixedDateTaskConverter;
    }

    @RequestMapping(value = "/server/bat/save", method = RequestMethod.POST)
    public ResultVO<Long> save(@RequestBody HokageFixedDateTaskForm form) {
        HokageFixedDateTaskDO taskDO = fixedDateTaskConverter.doForward(form);
        ServiceResponse<Long> response = fixedDateTaskService.upsert(taskDO);
        return response(response);
    }
}
