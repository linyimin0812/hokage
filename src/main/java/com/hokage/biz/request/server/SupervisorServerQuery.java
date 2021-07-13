package com.hokage.biz.request.server;

import com.hokage.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yiminlin
 * @date 2021/07/13 9:41 pm
 * @description supervisor server query
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SupervisorServerQuery extends PageQuery {
    private Long supervisorId;
}
