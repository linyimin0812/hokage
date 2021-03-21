package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageServerSshKeyContentDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author linyimin
 * @date 2021/03/21 20:18 pm
 * @email linyimin520812@gmail.com
 * @description server info mapper interface
 */

@Mapper
@Component
public interface HokageServerSshKeyContentMapper {
    /**
     * insert a new record
     * @param contentDO ssh key file content do
     * @return effective record number
     */
    Integer insert(HokageServerSshKeyContentDO contentDO);

    /**
     * retrieve a record based-on uid
     * @param uid uid
     * @return ssh key file content
     */
    HokageServerSshKeyContentDO listByUid(String uid);
}
