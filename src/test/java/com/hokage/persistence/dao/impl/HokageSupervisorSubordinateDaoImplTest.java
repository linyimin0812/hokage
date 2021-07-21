package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSupervisorSubordinateDao;
import com.hokage.persistence.dataobject.HokageSupervisorSubordinateDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 16:55
 * @email linyimin520812@gmail.com
 * @description HokageSupervisorSubordinateDaoImplTest
 */
@Import(HokageSupervisorSubordinateDaoImpl.class)
public class HokageSupervisorSubordinateDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageSupervisorSubordinateDao supervisorSubordinateDao;
    @Test
    @Rollback
    public void insert() {
        HokageSupervisorSubordinateDO supervisorSubordinateDO = new HokageSupervisorSubordinateDO();
        supervisorSubordinateDO.setId(hokageSequenceService.nextValue("hokage_supervisor_subordinate").getData());
        supervisorSubordinateDO.setSubordinateId(12L);
        supervisorSubordinateDO.setSupervisorId(56L);
        supervisorSubordinateDO.setStatus(0);

        Long result = supervisorSubordinateDao.insert(supervisorSubordinateDO);

        Assert.assertTrue(result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOs = supervisorSubordinateDao.listAll();
        supervisorSubordinateDOs.forEach(supervisorSubordinateDO -> {
            supervisorSubordinateDO.setSupervisorId(78L);
            supervisorSubordinateDao.update(supervisorSubordinateDO);
        });
        supervisorSubordinateDOs = supervisorSubordinateDao.listBySupervisorId(78L);

        Assert.assertTrue(supervisorSubordinateDOs.size() > 0);
    }

    @Test
    @Rollback
    public void selectById() {
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOs = supervisorSubordinateDao.listAll();
        supervisorSubordinateDOs.forEach(supervisorSubordinateDO -> Assert.assertNotEquals(null, supervisorSubordinateDao.listById(supervisorSubordinateDO.getId())));
    }

    @Test
    @Rollback
    public void selectAll() {
        int count = supervisorSubordinateDao.listAll().size();
        this.insert();
        Assert.assertEquals(count + 1, supervisorSubordinateDao.listAll().size());

        this.insert();
        Assert.assertEquals(count + 2, supervisorSubordinateDao.listAll().size());
    }

    @Test
    @Rollback
    public void selectBySupervisorId() {
        this.insert();
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOS = supervisorSubordinateDao.listBySupervisorId(56L);
        Assert.assertTrue(supervisorSubordinateDOS.size() > 0);
    }

    @Test
    @Rollback
    public void selectBySubordinateId() {
        this.insert();
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOS = supervisorSubordinateDao.listBySubordinateId(12L);
        supervisorSubordinateDao.listAll();
        Assert.assertTrue(supervisorSubordinateDOS.size() > 0);
    }
}