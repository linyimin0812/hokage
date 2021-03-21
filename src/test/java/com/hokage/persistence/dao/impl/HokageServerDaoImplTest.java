package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linyimin
 * @date 2020/8/23 16:35
 * @email linyimin520812@gmail.com
 * @description HokageServerDaoImplTest
 */

@Import(HokageServerDaoImpl.class)
public class HokageServerDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageServerDao serverDao;

    @Test
    @Rollback
    public void insert() {

        HokageServerDO serverDO = new HokageServerDO();
        serverDO.setId(hokageSequenceService.nextValue("hokage_server").getData());
        serverDO.setAccount("banzhe");
        serverDO.setDescription("My Server");
        serverDO.setDomain("node1.pcncad.cn");
        serverDO.setServerGroup("dev");
        serverDO.setHostname("node1");
        serverDO.setIp("10.108.210.102");
        serverDO.setPasswd("123456");
        serverDO.setSshPort("22");
        serverDO.setLoginType(1);
        Long result = serverDao.insert(serverDO);

        Assert.assertEquals(true, result > 0);

    }

    @Test
    @Rollback
    public void update() {
        this.insert();

        List<HokageServerDO> serverDOs = serverDao.selectAll();
        Assert.assertEquals(true, serverDOs.size() > 0);

        serverDOs.forEach(hokageServerDO -> {
            hokageServerDO.setAccount("linyimin");
            Long id = serverDao.update(hokageServerDO);
            Assert.assertEquals(true, id > 0);
        });

        serverDOs = serverDao.selectAll();
        Assert.assertEquals(true, serverDOs.size() > 0);

        serverDOs.forEach(hokageServerDO -> {
            hokageServerDO.setAccount("linyimin");
            Long id = serverDao.update(hokageServerDO);
            Assert.assertEquals("linyimin", hokageServerDO.getAccount());
        });
    }

    @Test
    @Rollback
    public void selectAll() {
        int count = serverDao.selectAll().size();
        this.insert();
        Assert.assertEquals(count + 1, serverDao.selectAll().size());
        this.insert();
        Assert.assertEquals(count + 2, serverDao.selectAll().size());
    }

    @Test
    @Rollback
    public void selectByIds() {
        this.insert();
        this.insert();
        List<HokageServerDO> serverDOS = serverDao.selectAll();
        List<Long> ids = serverDOS.stream().map(serverDO -> serverDO.getId()).collect(Collectors.toList());

        List<HokageServerDO> result = serverDao.selectByIds(ids);

        Assert.assertEquals(result.size(), serverDOS.size());
    }

    @Test
    @Rollback
    public void selectById() {
        this.insert();
        this.insert();
        List<HokageServerDO> serverDOS = serverDao.selectAll();
        List<Long> ids = serverDOS.stream().map(serverDO -> serverDO.getId()).collect(Collectors.toList());
        HokageServerDO serverDO = serverDao.selectById(ids.get(0));
        Assert.assertEquals(serverDO.getId(), ids.get(0));
    }

    @Test
    @Rollback
    public void selectByType() {
        this.insert();
        List<HokageServerDO> serverDOS = serverDao.listByType("1");
        Assert.assertEquals(1, serverDOS.size());

        this.insert();
        serverDOS = serverDao.listByType("1");
        Assert.assertEquals(2, serverDOS.size());

    }

    @Test
    @Rollback
    public void selectByGroup() {
        this.insert();
        List<HokageServerDO> serverDOS = serverDao.selectByGroup("dev");
        Assert.assertEquals(1, serverDOS.size());

        this.insert();
        serverDOS = serverDao.selectByGroup("dev");
        Assert.assertEquals(2, serverDOS.size());
    }
}