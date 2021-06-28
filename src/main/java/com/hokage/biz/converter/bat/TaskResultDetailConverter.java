package com.hokage.biz.converter.bat;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.response.bat.TaskResultDetailVO;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageTaskResultDO;
import com.hokage.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author yiminlin
 * @date 2021/06/28 9:11 am
 * @description task result detail converter
 **/
@Component
public class TaskResultDetailConverter implements Converter<HokageTaskResultDO, TaskResultDetailVO> {

    private HokageServerDao serverDao;

    @Autowired
    public void setServerDao(HokageServerDao serverDao) {
        this.serverDao = serverDao;
    }

    @Override
    public TaskResultDetailVO doForward(HokageTaskResultDO taskResultDO) {

        TaskResultDetailVO detailVO = new TaskResultDetailVO();

        List<Long> serverIds = Collections.singletonList(taskResultDO.getExecServer());
        List<HokageServerDO> serverDOList = serverDao.selectByIds(serverIds);
        if (CollectionUtils.isEmpty(serverDOList)) {
            throw new RuntimeException("server is not exit, server id: " + taskResultDO.getExecServer());
        }
        detailVO.setServerIp(serverDOList.get(0).getIp());

        long start = Optional.ofNullable(taskResultDO.getStartTime()).orElse(0L);
        detailVO.setStartTime(TimeUtil.format(start, TimeUtil.DISPLAY_FORMAT));

        long end = Optional.ofNullable(taskResultDO.getEndTime()).orElse(0L);
        if (end > 0) {
            detailVO.setCost(end - start);
            detailVO.setEndTime(TimeUtil.format(end, TimeUtil.DISPLAY_FORMAT));
        }

        detailVO.setStatus(taskResultDO.getStatus());
        detailVO.setTaskStatus(taskResultDO.getTaskStatus());

        return detailVO;
    }

    @Override
    public HokageTaskResultDO doBackward(TaskResultDetailVO taskResultDetailVO) {
        return null;
    }
}
