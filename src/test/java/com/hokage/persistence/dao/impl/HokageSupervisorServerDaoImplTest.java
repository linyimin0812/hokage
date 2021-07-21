package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSupervisorServerDao;
import com.hokage.persistence.dataobject.HokageSupervisorServerDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.Collections;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 16:55
 * @email linyimin520812@gmail.com
 * @description HokageSupervisorServerDaoImplTest
 */
@Import(HokageSupervisorServerDaoImpl.class)
public class HokageSupervisorServerDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageSupervisorServerDao supervisorServerDao;

    @Test
    @Rollback
    public void insert() {
        HokageSupervisorServerDO supervisorServerDO = new HokageSupervisorServerDO();
        supervisorServerDO.setId(hokageSequenceService.nextValue("hokage_supervisor_server").getData());
        supervisorServerDO.setServerId(12L);
        supervisorServerDO.setSupervisorId(56L);
        supervisorServerDO.setStatus(0);
        Long result = supervisorServerDao.insert(supervisorServerDO);
        Assert.assertTrue(result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();

        List<HokageSupervisorServerDO> supervisorServerDOS = supervisorServerDao.listByServerIds(Collections.singletonList(12L));

        supervisorServerDOS.forEach(item -> {
            item.setServerId(1L);
            supervisorServerDao.update(item);
        });

        supervisorServerDOS = supervisorServerDao.listByServerIds(Collections.singletonList(1L));
        Assert.assertNotEquals(supervisorServerDOS, null);
        Assert.assertTrue(supervisorServerDOS.size() > 0);
    }

    @Test
    @Rollback
    public void selectByServerId() {

        this.insert();
        Assert.assertTrue(supervisorServerDao.listByServerIds(Collections.singletonList(12L)).size() > 0);
    }

    @Test
    @Rollback
    public void selectBySupervisorId() {
        this.insert();
        Assert.assertTrue(supervisorServerDao.listBySupervisorIds(Collections.singletonList(56L)).size() > 0);
    }
}