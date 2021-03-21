package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageServerSshKeyContentDO;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 am
 * @email linyimin520812@gmail.com
 * @description server ssh key content dao interface
 */
public interface HokageServerSshKeyContentDao {
    /**
     * insert a new record
     * @param contentDO ssh key file content do
     * @return effective line number
     */
    Integer insert(HokageServerSshKeyContentDO contentDO);

    /**
     * retrieve ssh file content based-on uid
     * @param uid unique id
     * @return ssh key file content do
     */
    HokageServerSshKeyContentDO listByUid(String uid);
}
