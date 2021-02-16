package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageTaskDao;
import com.hokage.persistence.dataobject.HokageTaskDO;
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
 * @description HokageTaskDaoImplTest
 */
@Import(HokageTaskDaoImpl.class)
public class HokageTaskDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageTaskDao taskDao;

    @Test
    @Rollback
    public void insert() {

        HokageTaskDO taskDO = new HokageTaskDO();
        taskDO.setId(hokageSequenceService.nextValue("hokage_task").getData());
        taskDO.setDescription("test task");
        taskDO.setExecCommand("ls -l");
        taskDO.setExecServers("10.108.210.102, 10.108.210.106");
        taskDO.setExecTime(1576866660000L);
        taskDO.setExecType(1);
        taskDO.setTaskName("look file directory");
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
        taskDO.setDescription("test task");
        taskDO.setExecCommand("ls -l");
        taskDO.setExecServers("10.108.210.102, 10.108.210.106");
        taskDO.setExecTime(1576866660000L);
        taskDO.setExecType(1);
        taskDO.setTaskName("look file directory");
        taskDO.setTaskType(1);
        taskDO.setUserId(12L);

        List<HokageTaskDO> taskDOList = taskDao.findAll(taskDO);

        Assert.assertEquals(true, taskDOList.size() > 0);

    }
}