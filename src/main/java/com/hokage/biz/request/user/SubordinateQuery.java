package com.hokage.biz.request.user;

import com.hokage.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/07/12 8:46 pm
 * @description subordinate query
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SubordinateQuery extends PageQuery {
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
    private String subordinateName;
    /**
     * server group
     */
    private List<String> serverGroup;
}
