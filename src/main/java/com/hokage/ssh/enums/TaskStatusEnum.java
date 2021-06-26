package com.hokage.ssh.enums;

/**
 * @author yiminlin
 * @date 2021/06/27 7:06 am
 * @description task status
 **/
public enum TaskStatusEnum {

    /**
     * task status
     */
    offline(0),
    online(1);

    private Integer status;

    TaskStatusEnum(Integer status) {
        this.status = status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
