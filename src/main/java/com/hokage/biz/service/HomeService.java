package com.hokage.biz.service;

import com.hokage.biz.response.home.HomeDetailVO;
import com.hokage.common.ServiceResponse;

/**
 * @author yiminlin
 * @date 2021/07/19 9:21 pm
 * @description home service
 **/
public interface HomeService {
    /**
     * @return {@link HomeDetailVO}
     */
    ServiceResponse<HomeDetailVO> homeDetail();
}
