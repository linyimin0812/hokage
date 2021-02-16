package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageUserDao;
import com.hokage.persistence.dataobject.HokageUserDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 16:56
 * @email linyimin520812@gmail.com
 * @description HokageUserDaoImplTest
 */
@Import(HokageUserDaoImpl.class)
public class HokageUserDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageUserDao userDao;

    @Test
    @Rollback
    public void insert() {
        HokageUserDO userDO = new HokageUserDO();
        userDO.setId(hokageSequenceService.nextValue("hokage_user").getData());
        userDO.setSubscribed(1);
        userDO.setRole(1);
        userDO.setPasswd("123456");
        userDO.setEmail("banzhe@gmail.com");
        userDO.setUsername("banzhe");

        Long result = userDao.insert(userDO);

        Assert.assertEquals(true, result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();
        List<HokageUserDO> userDOList = userDao.listAll(new HokageUserDO());
        userDOList.forEach(hokageUserDO -> {
            hokageUserDO.setUsername("linyimin");
            userDao.update(hokageUserDO);
        });

        userDOList = userDao.listAll(new HokageUserDO());
        userDOList.forEach(hokageUserDO -> {
            Assert.assertEquals("linyimin", hokageUserDO.getUsername());
        });
    }

    @Test
    @Rollback
    public void findById() {
        this.insert();
        List<HokageUserDO> userDOList = userDao.listAll(new HokageUserDO());
        userDOList.forEach(hokageUserDO -> {
            HokageUserDO userDO = userDao.getUserById(hokageUserDO.getId());
            Assert.assertNotEquals(userDO, null);
        });
    }

    @Test
    @Rollback
    public void findByName() {
        this.insert();
        List<HokageUserDO> userDOList = userDao.listAll(new HokageUserDO());
        userDOList.forEach(hokageUserDO -> {
            List<HokageUserDO> userDOs = userDao.listUserByName(hokageUserDO.getUsername());
            Assert.assertNotEquals(userDOs, null);
        });
    }

    @Test
    @Rollback
    public void findByRole() {
        this.insert();
        List<HokageUserDO> userDOList = userDao.listAll(new HokageUserDO());
        userDOList.forEach(hokageUserDO -> {
            List<HokageUserDO> userDOs = userDao.listUserByRole(hokageUserDO.getRole());
            Assert.assertEquals(true, userDOs.size() > 0);
        });
    }

    @Test
    @Rollback
    public void findAll() {
        this.insert();
        HokageUserDO userDO = new HokageUserDO();
        userDO.setSubscribed(1);
        userDO.setRole(1);
        userDO.setPasswd("123456");
        userDO.setEmail("banzhe@gmail.com");
        userDO.setUsername("banzhe");
        List<HokageUserDO> userDOList = userDao.listAll(userDO);

        Assert.assertEquals(true, userDOList.size() > 0);
    }
}