package com.hokage.biz.form.server;

import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 5:38 pm
 * @email linyimin520812@gmail.com
 * @description server operate form
 */
@Data
public class ServerOperateForm {
    /**
     * operator id
     */
    private Long operatorId;
    /**
     * user ids
     */
    private List<Long> userIds;
    /**
     * server ids
     */
    private List<Long> serverIds;

    /**
     * server group
     */
    private ServerGroupForm serverGroup;

    @Data
    public static class ServerGroupForm {
        /**
         * primary key
         */
        private Long id;
        /**
         * group name
         */
        private String name;
        /**
         * creator id
         */
        private Long creatorId;

        /**
         * description
         */
        private String description;
    }

}
