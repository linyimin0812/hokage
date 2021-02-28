package com.hokage.biz.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author linyimin
 * @date 2021/2/17 18:04
 * @email linyimin520812@gmail.com
 * @description hokage option
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class HokageOptionVO<T> {
    private String label;
    private T value;

    public HokageOptionVO() {}
}
