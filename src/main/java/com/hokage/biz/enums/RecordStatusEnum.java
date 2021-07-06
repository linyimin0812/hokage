package com.hokage.biz.enums;

/**
 * @author yiminlin
 * @date 2021/07/06 9:13 am
 * @description record status
 **/
public enum RecordStatusEnum {


    /**
     * record status
     */
    deleted(-1),
    inuse(0)
    ;

    private final Integer status;

    RecordStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
