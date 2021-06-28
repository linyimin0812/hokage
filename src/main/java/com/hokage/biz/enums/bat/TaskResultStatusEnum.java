package com.hokage.biz.enums.bat;

/**
 * @author yiminlin
 * @date 2021/06/28 9:25 pm
 * @description task result status enum
 **/
public enum TaskResultStatusEnum {
    /**
     * task status
     */
    running(0),
    finished(1),
    failed(-1);

    private Integer status;

    TaskResultStatusEnum(Integer status) {
        this.status = status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
