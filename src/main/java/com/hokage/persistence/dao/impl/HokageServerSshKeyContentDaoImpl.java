package com.hokage.persistence.dao.impl;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageSequenceDao;
import com.hokage.persistence.dao.HokageServerSshKeyContentDao;
import com.hokage.persistence.dataobject.HokageServerSshKeyContentDO;
import com.hokage.persistence.mapper.HokageServerSshKeyContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 am
 * @email linyimin520812@gmail.com
 * @description server ssh key content dao implementation
 */
@Repository
public class HokageServerSshKeyContentDaoImpl implements HokageServerSshKeyContentDao {

    private HokageServerSshKeyContentMapper mapper;

    private HokageSequenceService sequenceService;

    @Autowired
    public void setMapper(HokageServerSshKeyContentMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setSequenceDao(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public Integer insert(HokageServerSshKeyContentDO contentDO) {
        ServiceResponse<Long> response = sequenceService.nextValue(SequenceNameEnum.hokage_server_ssh_key_content.name());
        if (!response.getSucceeded() || Objects.isNull(response.getData())) {
            throw new RuntimeException("get ssh key content id error. reason: " + JSON.toJSONString(response));
        }
        contentDO.setId(response.getData());
        return mapper.insert(contentDO);
    }

    @Override
    public HokageServerSshKeyContentDO listByUid(String uid) {
        return mapper.listByUid(uid);
    }
}
