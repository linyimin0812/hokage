package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageTaskResultDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:33 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Mapper
@Component
public interface HokageTaskResultMapper {
    /**
     * 插入一条任务执行结果
     * @param hokageTaskResultDO
     * @return
     */
    Integer insert(HokageTaskResultDO hokageTaskResultDO);

    /**
     * 更新一条任务执行结果
     * @param hokageTaskResultDO
     * @return
     */
    Integer update(HokageTaskResultDO hokageTaskResultDO);

    /**
     * 根据id主键查找任务执行结果
     * @param id
     * @return
     */
    HokageTaskResultDO findById(Long id);

    /**
     * 根据任务id查找任务执行结果
     * @param taskId
     * @return
     */
    List<HokageTaskResultDO> findByTaskId(Long taskId);
}
