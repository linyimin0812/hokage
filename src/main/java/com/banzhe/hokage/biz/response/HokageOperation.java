package com.banzhe.hokage.biz.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/10/28 12:22 am
 * @email linyimin520812@gmail.com
 * @description action type
 */
@Data
@AllArgsConstructor
public class HokageOperation {
    /**
     * action type: modal、confirm dialog、link
     */
    private String operationType;
    /**
     * action name
     */
    private String operationName;
    /**
     * action link
     */
    private String operationLink;

}
