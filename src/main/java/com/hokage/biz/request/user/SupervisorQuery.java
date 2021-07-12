package com.hokage.biz.request.user;

import com.hokage.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/07/12 7:24 pm
 * @description supervisor query
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SupervisorQuery extends PageQuery {
    /**
     * operator id
     */
    private Long operatorId;

    /**
     * primary key
     */
    private Long id;
    /**
     * supervisor name
     */
    private String supervisorName;
    /**
     * server group
     */
    private List<String> serverGroup;
}
