package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageServerGroupDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerGroupDO;
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
 * @description HokageServerGroupDaoImplTest
 */
@Import(HokageServerGroupDaoImpl.class)
public class HokageServerGroupDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageServerGroupDao hokageServerGroupDao;

    @Test
    @Rollback
    public void insert() {
        HokageServerGroupDO serverGroupDO = new HokageServerGroupDO();
        serverGroupDO.setId(hokageSequenceService.nextValue("hokage_server_group").getData());
        serverGroupDO.setDescription("开发测试");
        serverGroupDO.setName("dev");
        Long id = hokageServerGroupDao.insert(serverGroupDO);
        Assert.assertEquals(true, id > 0);
    }

    @Test
    @Rollback
    public void selectAll() {
        int count = hokageServerGroupDao.selectAll().size();
        this.insert();
        Assert.assertEquals(count + 1, hokageServerGroupDao.selectAll().size());

        this.insert();
        Assert.assertEquals(count + 2, hokageServerGroupDao.selectAll().size());
    }

    @Test
    public void update() {
        this.insert();
        List<HokageServerGroupDO> serverGroupDOS = hokageServerGroupDao.selectAll();

        serverGroupDOS.forEach(hokageServerGroupDO -> {
            hokageServerGroupDO.setName("prod");
            hokageServerGroupDao.update(hokageServerGroupDO);
        });

        hokageServerGroupDao.selectAll().forEach(hokageServerGroupDO -> {
            Assert.assertEquals("prod", hokageServerGroupDO.getName());
        });

    }
}