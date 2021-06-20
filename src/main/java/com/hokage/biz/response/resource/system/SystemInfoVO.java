package com.hokage.biz.response.resource.system;

import lombok.Data;
import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/19 4:01 pm
 * @description system info
 **/
@Data
public class SystemInfoVO {
    private List<ProcessInfoVO> processInfo;
    private List<DiskInfoVO> diskInfo;
}
