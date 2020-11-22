package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.biz.service.impl.HokageSequenceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author linyimin
 * @date 2020/8/23 16:43
 * @email linyimin520812@gmail.com
 * @description The base test class
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        HokageSequenceServiceImpl.class,
        HokageSequenceDaoImpl.class
})
public class HokageBaseDaoTest {
    @Autowired
    protected HokageSequenceService hokageSequenceService;

    @Test
    public void init(){}
}
