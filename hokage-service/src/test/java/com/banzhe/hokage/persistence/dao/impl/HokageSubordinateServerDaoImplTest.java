package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSubordinateServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author linyimin
 * @date 2020/8/23 4:55 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Import(HokageSubordinateServerDaoImpl.class)
public class HokageSubordinateServerDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageSubordinateServerDao subordinateServerDao;
    @Test
    @Rollback
    public void insert() {
        HokageSubordinateServerDO subordinateServerDO = new HokageSubordinateServerDO();

        subordinateServerDO.setServerId(12312L);
        subordinateServerDO.setSubordinateId(2L);

        Long result = subordinateServerDao.insert(subordinateServerDO);

        Assert.assertEquals(true, result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();

        List<HokageSubordinateServerDO> subordinateServerDOS = subordinateServerDao.selectByServerId(12312L);

        subordinateServerDOS.forEach(item -> {
            item.setServerId(1L);
            subordinateServerDao.update(item);
        });

        subordinateServerDOS = subordinateServerDao.selectByServerId(1L);
        Assert.assertNotEquals(subordinateServerDOS, null);
    }

    @Test
    @Rollback
    public void selectByServerId() {
        this.insert();
        Assert.assertEquals(true, subordinateServerDao.selectByServerId(12312L).size() > 0);
    }

    @Test
    @Rollback
    public void selectByOrdinateId() {
        this.insert();
        Assert.assertEquals(true, subordinateServerDao.selectByOrdinateId(2L).size() > 0);
    }
}