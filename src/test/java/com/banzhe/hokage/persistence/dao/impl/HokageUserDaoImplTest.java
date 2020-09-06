package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 4:56 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Import(HokageUserDaoImpl.class)
public class HokageUserDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageUserDao userDao;

    @Test
    @Rollback
    public void insert() {
        HokageUserDO userDO = new HokageUserDO();
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
            List<HokageUserDO> userDOs = userDao.ListUserByName(hokageUserDO.getUsername());
            Assert.assertNotEquals(userDOs, null);
        });
    }

    @Test
    @Rollback
    public void findByRole() {
        this.insert();
        List<HokageUserDO> userDOList = userDao.listAll(new HokageUserDO());
        userDOList.forEach(hokageUserDO -> {
            List<HokageUserDO> userDOs = userDao.ListUserByRole(hokageUserDO.getRole());
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