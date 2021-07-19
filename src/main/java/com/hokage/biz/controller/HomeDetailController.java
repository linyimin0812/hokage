package com.hokage.biz.controller;

import com.hokage.biz.response.home.HomeDetailVO;
import com.hokage.biz.service.HomeService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
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

    @Autowired
    public void setHomeService(HomeService homeService) {
        this.homeService = homeService;
    }

    @RequestMapping(value = "/home/detail", method = RequestMethod.GET)
    public ResultVO<HomeDetailVO> homeDetail() {
        ServiceResponse<HomeDetailVO> response = homeService.homeDetail();
        return response(response);
    }
}
