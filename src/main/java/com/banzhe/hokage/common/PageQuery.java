package com.banzhe.hokage.common;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/11/1 4:56 下午
 * @email linyimin520812@gmail.com
 * @description 分页查询
 */
@Data
public class PageQuery {
    private Integer pageNum;
    private Integer pageSize;
}
