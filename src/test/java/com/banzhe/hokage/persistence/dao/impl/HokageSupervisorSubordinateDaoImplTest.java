package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSupervisorSubordinateDao;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorSubordinateDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.Assert.*;

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

        Long result = supervisorSubordinateDao.insert(supervisorSubordinateDO);

        Assert.assertEquals(true, result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOs = supervisorSubordinateDao.selectAll();
        supervisorSubordinateDOs.forEach(supervisorSubordinateDO -> {
            supervisorSubordinateDO.setSupervisorId(78L);
            supervisorSubordinateDao.update(supervisorSubordinateDO);
        });
        supervisorSubordinateDOs = supervisorSubordinateDao.selectBySupervisorId(78L);

        Assert.assertEquals(true, supervisorSubordinateDOs.size() > 0);
    }

    @Test
    @Rollback
    public void selectById() {
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOs = supervisorSubordinateDao.selectAll();
        supervisorSubordinateDOs.forEach(supervisorSubordinateDO -> {
            Assert.assertNotEquals(null, supervisorSubordinateDao.selectById(supervisorSubordinateDO.getId()));
        });
    }

    @Test
    @Rollback
    public void selectAll() {
        int count = supervisorSubordinateDao.selectAll().size();
        this.insert();
        Assert.assertEquals(count + 1, supervisorSubordinateDao.selectAll().size());

        this.insert();
        Assert.assertEquals(count + 2, supervisorSubordinateDao.selectAll().size());
    }

    @Test
    @Rollback
    public void selectBySupervisorId() {
        this.insert();
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOS = supervisorSubordinateDao.selectBySupervisorId(56L);
        Assert.assertEquals(true, supervisorSubordinateDOS.size() > 0);
    }

    @Test
    @Rollback
    public void selectBySubordinateId() {
        this.insert();
        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOS = supervisorSubordinateDao.selectBySubordinateId(12L);
        supervisorSubordinateDao.selectAll();
        Assert.assertEquals(true, supervisorSubordinateDOS.size() > 0);
    }
}