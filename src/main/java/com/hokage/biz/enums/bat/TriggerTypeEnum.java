package com.hokage.biz.enums.bat;

/**
 * @author yiminlin
 * @date 2021/06/28 9:26 下午
 * @description trigger type enum
 **/
public enum TriggerTypeEnum {
    /**
     * task status
     */
    manual(0),
    auto_scheduled(1),
    delete(-1);

    private Integer status;

    TriggerTypeEnum(Integer status) {
        this.status = status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
