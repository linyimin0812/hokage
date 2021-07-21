package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSubordinateServerDao;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
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
 * @description HokageSubordinateServerDaoImplTest
 */
@Import(HokageSubordinateServerDaoImpl.class)
public class HokageSubordinateServerDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageSubordinateServerDao subordinateServerDao;
    @Test
    @Rollback
    public void insert() {
        HokageSubordinateServerDO subordinateServerDO = new HokageSubordinateServerDO();

        subordinateServerDO.setId(hokageSequenceService.nextValue("hokage_subordinate_server").getData());
        subordinateServerDO.setServerId(12312L);
        subordinateServerDO.setSubordinateId(2L);
        subordinateServerDO.setIp("0.0.0.0");
        subordinateServerDO.setSshPort("22");
        subordinateServerDO.setAccount("root");
        subordinateServerDO.setPasswd("123456");
        subordinateServerDO.setLoginType(0);
        subordinateServerDO.setStatus(0);
        Long result = subordinateServerDao.insert(subordinateServerDO);

        Assert.assertTrue(result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();

        List<HokageSubordinateServerDO> subordinateServerDOS = subordinateServerDao.listByServerIds(Collections.singletonList(12312L));

        subordinateServerDOS.forEach(item -> {
            item.setServerId(1L);
            subordinateServerDao.update(item);
        });

        subordinateServerDOS = subordinateServerDao.listByServerIds(Collections.singletonList(1L));
        Assert.assertNotEquals(subordinateServerDOS, null);
    }

    @Test
    @Rollback
    public void selectByServerId() {
        this.insert();
        Assert.assertTrue(subordinateServerDao.listByServerIds(Collections.singletonList(12312L)).size() > 0);
    }

    @Test
    @Rollback
    public void selectByOrdinateId() {
        this.insert();
        Assert.assertTrue(subordinateServerDao.listByOrdinateIds(Collections.singletonList(2L)).size() > 0);
    }
}