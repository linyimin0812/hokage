package com.banzhe.hokage.persistence.dao;

import com.banzhe.hokage.persistence.dataobject.HokageServerApplicationDO;

/**
 * @author linyimin
 * @date 2021/2/16 15:54
 * @email linyimin520812@gmail.com
 * @description hokage server application dao interface
 */

public interface HokageServerApplicationDao {
    /**
     * insert or update a server application record
     * @param applicationDO
     * @return
     */
    boolean save(HokageServerApplicationDO applicationDO);
}
