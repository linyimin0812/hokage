package com.banzhe.hokage.persistence.dao.impl;

import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author linyimin
 * @date 2020/8/23 4:43 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HokageBaseDaoTest {
}
