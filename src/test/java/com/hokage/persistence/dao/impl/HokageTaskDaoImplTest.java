package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageFixedDateTaskDao;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
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
    private HokageFixedDateTaskDao taskDao;

    @Test
    @Rollback
    public void insert() {

        HokageFixedDateTaskDO taskDO = new HokageFixedDateTaskDO();
        taskDO.setId(hokageSequenceService.nextValue("hokage_task").getData());
        taskDO.setExecCommand("ls -l");
        taskDO.setExecServers("10.108.210.102, 10.108.210.106");
        taskDO.setExecTime(1576866660000L);
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
        List<HokageFixedDateTaskDO> taskDOList = taskDao.findAll(new HokageFixedDateTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            hokageTaskDO.setUserId(56L);
            taskDao.update(hokageTaskDO);
        });

        taskDOList = taskDao.findAll(new HokageFixedDateTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            Assert.assertEquals(hokageTaskDO.getUserId() + 0, 56L);
        });
    }

    @Test
    @Rollback
    public void findById() {

        this.insert();
        List<HokageFixedDateTaskDO> taskDOList = taskDao.findAll(new HokageFixedDateTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            HokageFixedDateTaskDO taskDO = taskDao.findById(hokageTaskDO.getId());
            Assert.assertNotEquals(taskDO, null);
        });

    }

    @Test
    @Rollback
    public void findByName() {
        this.insert();
        List<HokageFixedDateTaskDO> taskDOList = taskDao.findAll(new HokageFixedDateTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            List<HokageFixedDateTaskDO> taskDO = taskDao.findByName(hokageTaskDO.getTaskName());
            Assert.assertNotEquals(taskDO, null);
        });
    }

    @Test
    @Rollback
    public void findByType() {
        this.insert();
        List<HokageFixedDateTaskDO> taskDOList = taskDao.findAll(new HokageFixedDateTaskDO());
        taskDOList.forEach(hokageTaskDO -> {
            List<HokageFixedDateTaskDO> taskDOs = taskDao.findByType(hokageTaskDO.getTaskType());
            Assert.assertEquals(true, taskDOs.size() > 0);
        });
    }

    @Test
    @Rollback
    public void findAll() {

        this.insert();

        HokageFixedDateTaskDO taskDO = new HokageFixedDateTaskDO();
        taskDO.setExecCommand("ls -l");
        taskDO.setExecServers("10.108.210.102, 10.108.210.106");
        taskDO.setExecTime(1576866660000L);
        taskDO.setTaskName("look file directory");
        taskDO.setTaskType(1);
        taskDO.setUserId(12L);

        List<HokageFixedDateTaskDO> taskDOList = taskDao.findAll(taskDO);

        Assert.assertEquals(true, taskDOList.size() > 0);

    }
}