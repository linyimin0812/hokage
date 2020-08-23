package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageTaskDao;
import com.banzhe.hokage.persistence.dao.HokageTaskResultDao;
import com.banzhe.hokage.persistence.dataobject.HokageTaskDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author linyimin
 * @date 2020/8/23 4:56 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Import(HokageTaskDaoImpl.class)
public class HokageTaskDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageTaskDao taskDao;

    @Test
    @Rollback
    public void insert() {

        HokageTaskDO taskDO = new HokageTaskDO();
        taskDO.setDescription("测试任务");
        taskDO.setExecCommand("ls -l");
        taskDO.setExecServers("10.108.210.102, 10.108.210.106");
        taskDO.setExecTime(1576866660000L);
        taskDO.setExecType(1);
        taskDO.setTaskName("查看文件目录");
        taskDO.setTaskType(1);
        taskDO.setUserId(12L);

        Long result = taskDao.insert(taskDO);

        Assert.assertEquals(true, result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();
        List<HokageTaskDO> taskDOList = taskDao.findAll(new HokageTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            hokageTaskDO.setUserId(56L);
            taskDao.update(hokageTaskDO);
        });

        taskDOList = taskDao.findAll(new HokageTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            Assert.assertEquals(hokageTaskDO.getUserId() + 0, 56L);
        });
    }

    @Test
    @Rollback
    public void findById() {

        this.insert();
        List<HokageTaskDO> taskDOList = taskDao.findAll(new HokageTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            HokageTaskDO taskDO = taskDao.findById(hokageTaskDO.getId());
            Assert.assertNotEquals(taskDO, null);
        });

    }

    @Test
    @Rollback
    public void findByName() {
        this.insert();
        List<HokageTaskDO> taskDOList = taskDao.findAll(new HokageTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            HokageTaskDO taskDO = taskDao.findByName(hokageTaskDO.getTaskName());
            Assert.assertNotEquals(taskDO, null);
        });
    }

    @Test
    @Rollback
    public void findByType() {
        this.insert();
        List<HokageTaskDO> taskDOList = taskDao.findAll(new HokageTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            List<HokageTaskDO> taskDOs = taskDao.findByType(hokageTaskDO.getTaskType());
            Assert.assertEquals(true, taskDOs.size() > 0);
        });
    }

    @Test
    @Rollback
    public void findAll() {

        this.insert();

        HokageTaskDO taskDO = new HokageTaskDO();
        taskDO.setDescription("测试任务");
        taskDO.setExecCommand("ls -l");
        taskDO.setExecServers("10.108.210.102, 10.108.210.106");
        taskDO.setExecTime(1576866660000L);
        taskDO.setExecType(1);
        taskDO.setTaskName("查看文件目录");
        taskDO.setTaskType(1);
        taskDO.setUserId(12L);

        List<HokageTaskDO> taskDOList = taskDao.findAll(taskDO);

        Assert.assertEquals(true, taskDOList.size() > 0);

    }
}