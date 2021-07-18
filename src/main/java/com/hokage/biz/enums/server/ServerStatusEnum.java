package com.hokage.biz.enums.server;

/**
 * @author yiminlin
 * @date 2021/07/18 4:52 pm
 * @description server status
 **/
public enum ServerStatusEnum {
    /**
     * server status
     */
    unknown(-2),
    offline(-1),
    online(0)
    ;

    private final Integer status;

    ServerStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
