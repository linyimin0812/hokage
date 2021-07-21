package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSequenceDao;
import com.hokage.persistence.dataobject.HokageSequenceDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

/**
 * @author linyimin
 * @date 2020/8/30 16:59
 * @email linyimin520812@gmail.com
 * @description HokageSequenceDaoImplTest
 */
@Import(HokageSequenceDaoImpl.class)
public class HokageSequenceDaoImplTest extends HokageBaseDaoTest{

    @Autowired
    private HokageSequenceDao sequenceDao;

    @Test
    @Rollback
    public void insert() {
        HokageSequenceDO sequenceDO = new HokageSequenceDO();
        sequenceDO.setName("banzhe");
        sequenceDO.setValue(10000L);

        Integer result = sequenceDao.insert(sequenceDO);

        Assert.assertTrue(result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();
        HokageSequenceDO sequenceDO = sequenceDao.getSequenceByName("banzhe");
        Assert.assertNotEquals(null, sequenceDO);

        sequenceDO.setValue(1111L);

        sequenceDO = sequenceDao.getSequenceByName("banzhe");
        Assert.assertNotEquals(null, sequenceDO);

        Assert.assertTrue(sequenceDO.getValue() == 1111L);
    }

    @Test
    @Rollback
    public void getSequenceByName() {
        this.insert();
        HokageSequenceDO sequenceDO = sequenceDao.getSequenceByName("banzhe");
        Assert.assertNotEquals(null, sequenceDO);
    }
}