package com.hokage.biz.response.resource.general;

import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/16 12:48 am
 * @description server base information
 **/
@Data
public class BasicInfoVO {
    private List<GeneralInfoVO> cpuInfo;
    private List<GeneralInfoVO> memInfo;
    private List<AccountInfoVO> accountInfo;
    private List<LastLogInfoVO> lastLogInfo;
    private List<GeneralInfoVO> generalInfo;
}
