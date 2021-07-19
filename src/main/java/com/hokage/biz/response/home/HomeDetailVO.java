package com.hokage.biz.response.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yiminlin
 * @date 2021/07/19 9:16 pm
 * @description home detail vo
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeDetailVO {
    private HomeDetailMeta allVO;
    private HomeDetailMeta availableVO;
    private HomeDetailMeta accountVO;
}
