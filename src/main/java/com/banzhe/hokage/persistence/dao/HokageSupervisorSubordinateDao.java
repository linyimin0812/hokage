package com.banzhe.hokage.persistence.dao;

import com.banzhe.hokage.persistence.dataobject.HokageSupervisorSubordinateDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:42 am
 * @email linyimin520812@gmail.com
 * @description supervisor and subordinate relationship mapping table Dao interface
 */
public interface HokageSupervisorSubordinateDao {

    /**
     * insert a new record
     * @param supervisorSubordinateDO
     * @return
     */
    Long insert(HokageSupervisorSubordinateDO supervisorSubordinateDO);

    /**
     * insert multiple new records
     * @param supervisorSubordinateDOS
     * @return
     */
    Long insertBatch(List<HokageSupervisorSubordinateDO> supervisorSubordinateDOS);

    /**
     * update a new record
     * @param supervisorSubordinateDO
     * @return
     */
    Long update(HokageSupervisorSubordinateDO supervisorSubordinateDO);

    /**
     * retrieve supervisor and subordinate relationship mapping information based on id
     * @param id
     * @return
     */
    HokageSupervisorSubordinateDO listById(Long id);

    /**
     * retrieve all supervisor and subordinate relationship mapping information
     * @return
     */
    List<HokageSupervisorSubordinateDO> listAll();

    /**
     * retrieve subordinate id based on supervisor id
     * @param id
     * @return
     */
    List<HokageSupervisorSubordinateDO> listBySupervisorId(Long id);

    /**
     * retrieve supervisor id based on subordinate id
     * @param id
     * @return
     */
    List<HokageSupervisorSubordinateDO> listBySubordinateId(Long id);
}
