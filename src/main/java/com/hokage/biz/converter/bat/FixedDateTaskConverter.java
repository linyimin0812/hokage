package com.hokage.biz.converter.bat;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.form.bat.HokageFixedDateTaskForm;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import com.hokage.biz.enums.bat.TaskStatusEnum;
import com.hokage.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/27 4:50 am
 * @description fixed date task converter
 **/
@Component
public class FixedDateTaskConverter implements Converter<HokageFixedDateTaskForm, HokageFixedDateTaskDO> {
    @Override
    public HokageFixedDateTaskDO doForward(HokageFixedDateTaskForm form) {
        HokageFixedDateTaskDO taskDO = new HokageFixedDateTaskDO();
        BeanUtils.copyProperties(form, taskDO);
        taskDO.setUserId(form.getOperatorId());
        try {
            taskDO.setExecTime(TimeUtil.timestamp(form.getExecTime(), "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        taskDO.setStatus(TaskStatusEnum.offline.getStatus());
        taskDO.setExecServers(form.getExecServers().stream().map(String::valueOf).collect(Collectors.joining(",")));
        return taskDO;
    }

    @Override
    public HokageFixedDateTaskForm doBackward(HokageFixedDateTaskDO fixedDateTaskDO) {
        return null;
    }
}
