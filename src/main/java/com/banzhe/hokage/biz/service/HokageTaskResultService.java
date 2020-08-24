package com.banzhe.hokage.biz.service;

import com.banzhe.hokage.persistence.dataobject.HokageTaskResultDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:33 下午
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageTaskResultService {
    /**
     * 插入一条任务执行结果
     * @param hokageTaskResultDO
     * @return
     */
    Long insert(HokageTaskResultDO hokageTaskResultDO);

    /**
     * 更新一条任务执行结果
     * @param hokageTaskResultDO
     * @return
     */
    Long update(HokageTaskResultDO hokageTaskResultDO);

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
