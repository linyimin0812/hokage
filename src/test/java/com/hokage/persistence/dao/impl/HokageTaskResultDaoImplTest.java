package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageTaskResultDao;
import com.hokage.persistence.dataobject.HokageTaskResultDO;
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
 * @description HokageTaskResultDaoImplTest
 */
@Import(HokageTaskResultDaoImpl.class)
public class HokageTaskResultDaoImplTest extends HokageBaseDaoTest {

    @Autowired
    private HokageTaskResultDao taskResultDao;

    @Test
    @Rollback
    public void insert() {
        HokageTaskResultDO taskResultDO = new HokageTaskResultDO();
        taskResultDO.setId(hokageSequenceService.nextValue("hokage_task_result").getData());
        taskResultDO.setEndTime(System.currentTimeMillis());
        taskResultDO.setStartTime(System.currentTimeMillis());
        taskResultDO.setExecServer(102L);
        taskResultDO.setExitCode(0);
        taskResultDO.setExecResult("true");
        taskResultDO.setTaskId(111L);
        taskResultDO.setTaskStatus(1);

        Long result = taskResultDao.insert(taskResultDO);

        Assert.assertEquals(true, result > 0);
    }

    @Test
    @Rollback
    public void update() {
        this.insert();
        List<HokageTaskResultDO> taskResultDOList = taskResultDao.findByTaskId(111L);
        taskResultDOList.forEach(hokageTaskResultDO -> {
            hokageTaskResultDO.setExitCode(1);
            taskResultDao.update(hokageTaskResultDO);
        });

        taskResultDOList = taskResultDao.findByTaskId(111L);
        taskResultDOList.forEach(hokageTaskResultDO -> {
            Assert.assertEquals(1, hokageTaskResultDO.getExitCode() + 0);
        });


    }

    @Test
    @Rollback
    public void findById() {
        this.insert();
        List<HokageTaskResultDO> taskResultDOList = taskResultDao.findByTaskId(111L);
        taskResultDOList.forEach(hokageTaskResultDO -> {
            HokageTaskResultDO resultDO = taskResultDao.findById(hokageTaskResultDO.getId());
            Assert.assertNotEquals(resultDO, null);
        });
    }

    @Test
    @Rollback
    public void findByTaskId() {
        this.insert();
        List<HokageTaskResultDO> taskResultDOList = taskResultDao.findByTaskId(111L);

        Assert.assertEquals(true, taskResultDOList.size() > 0);
    }
}