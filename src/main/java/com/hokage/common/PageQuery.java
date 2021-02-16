package com.hokage.common;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/11/1 4:56 pm
 * @email linyimin520812@gmail.com
 * @description page query
 */
@Data
public class PageQuery {
    private Integer pageNum;
    private Integer pageSize;
}
