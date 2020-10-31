package com.banzhe.hokage.biz.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/10/28 12:22 上午
 * @email linyimin520812@gmail.com
 * @description 操作类型
 */
@Data
@AllArgsConstructor
public class HokageOperation {
    private String operationType;   // 操作类型
    private String operationName;   // 操作名称
    private String operationLink;   // 操作链接
}
