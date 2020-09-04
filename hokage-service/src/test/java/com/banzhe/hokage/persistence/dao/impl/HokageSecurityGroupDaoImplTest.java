package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.biz.service.impl.HokageSequenceServiceImpl;
import com.banzhe.hokage.persistence.dao.HokageSecurityGroupDao;
import com.banzhe.hokage.persistence.dao.HokageSequenceDao;
import com.banzhe.hokage.persistence.dataobject.HokageSecurityGroupDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 1:47 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Import({
        HokageSecurityGroupDaoImpl.class,
        HokageSequenceServiceImpl.class,
        HokageSequenceDaoImpl.class
})
public class HokageSecurityGroupDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageSecurityGroupDao securityGroupDao;

    @Autowired
    private HokageSequenceService sequenceService;

    @Test
    @Rollback
    public void insert() {
        HokageSecurityGroupDO securityGroupDO = new HokageSecurityGroupDO();
        securityGroupDO.setId(sequenceService.nextValue("hokage_security_group").getData());
        securityGroupDO.setAuthObject("0.0.0,0/0");
        securityGroupDO.setAuthStrategy(1);
        securityGroupDO.setDescription("test");
        securityGroupDO.setPortRange("80-82");
        securityGroupDO.setProtocolType(2);
        securityGroupDO.setServers("10.108.210.195,10.108.210.196");
        securityGroupDO.setStatus(1);
        securityGroupDO.setUserId(12312321L);
        Long result = securityGroupDao.insert(securityGroupDO);
        Assert.assertEquals(true, result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();
        List<HokageSecurityGroupDO> securityGroupDOS = securityGroupDao.selectAll();
        securityGroupDOS.forEach(groupDO -> {
            Assert.assertEquals("0.0.0,0/0", groupDO.getAuthObject());
            groupDO.setAuthObject("0.0.0,0/1");
            Long result = securityGroupDao.update(groupDO);
            Assert.assertEquals(true, result > 0);
        });
        securityGroupDOS = securityGroupDao.selectAll();
        securityGroupDOS.forEach(groupDO -> {
            Assert.assertEquals("0.0.0,0/1", groupDO.getAuthObject());
        });
        Assert.assertEquals(1, securityGroupDOS.size());

    }

    @Test
    @Rollback
    public void selectAll() {
        int count = securityGroupDao.selectAll().size();
        this.insert();
        Assert.assertEquals(count + 1, securityGroupDao.selectAll().size());
        this.insert();
        Assert.assertEquals(count + 2, securityGroupDao.selectAll().size());

    }
}