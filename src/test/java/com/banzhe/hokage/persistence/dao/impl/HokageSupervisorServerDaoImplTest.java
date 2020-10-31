package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 4:55 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Import(HokageSupervisorServerDaoImpl.class)
public class HokageSupervisorServerDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageSupervisorServerDao supervisorServerDao;

    @Test
    @Rollback
    public void insert() {
        HokageSupervisorServerDO supervisorServerDO = new HokageSupervisorServerDO();
        supervisorServerDO.setServerId(12L);
        supervisorServerDO.setSupervisorId(56L);
        Long result = supervisorServerDao.insert(supervisorServerDO);
        Assert.assertEquals(true, result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();

        List<HokageSupervisorServerDO> supervisorServerDOS = supervisorServerDao.listByServerId(12L);

        supervisorServerDOS.forEach(item -> {
            item.setServerId(1L);
            supervisorServerDao.update(item);
        });

        supervisorServerDOS = supervisorServerDao.listByServerId(1L);
        Assert.assertNotEquals(supervisorServerDOS, null);
        Assert.assertEquals(true, supervisorServerDOS.size() > 0);
    }

    @Test
    @Rollback
    public void selectByServerId() {

        this.insert();
        Assert.assertEquals(true, supervisorServerDao.listByServerId(12L).size() > 0);
    }

    @Test
    @Rollback
    public void selectBySupervisorId() {
        this.insert();
        Assert.assertEquals(true, supervisorServerDao.listByIds(Arrays.asList(56L)).size() > 0);
    }
}