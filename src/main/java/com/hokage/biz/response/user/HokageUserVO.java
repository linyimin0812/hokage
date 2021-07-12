package com.hokage.biz.response.user;

import com.hokage.biz.response.HokageOperation;
import com.hokage.biz.response.server.HokageServerVO;
import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/10/28 12:26 am
 * @email linyimin520812@gmail.com
 * @description user management
 */
@Data
public class HokageUserVO {
    /**
     * user id
     */
    private Long id;
    /**
     * username
     */
    private String username;

    /**
     * user email
     */
    private String email;
    /**
     * user role
     */
    private Integer role;
    /**
     * number of server the user use or manage
     */
    private Integer serverNum;
    /**
     * groups of server
     */
    private List<String> serverGroupList;
    /**
     * operation list
     */
    List<HokageOperation> operationList;
    /**
     * detail of server the user use or manage
     */
    List<HokageServerVO> serverVOList;

    /**
     * supervisor id
     */
    private Long supervisorId;

    /**
     * supervisor name
     */
    private String supervisorName;
}
