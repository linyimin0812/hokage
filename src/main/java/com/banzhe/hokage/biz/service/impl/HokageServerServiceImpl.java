package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.service.HokageServerService;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/10/31 1:20 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Service
public class HokageServerServiceImpl implements HokageServerService {
    @Override
    public Long insert(HokageServerDO serverDO) {
        return null;
    }

    @Override
    public Long update(HokageServerDO serverDO) {
        return null;
    }

    @Override
    public List<HokageServerDO> selectAll() {
        return null;
    }

    @Override
    public List<HokageServerDO> selectByIds(List<Long> ids) {
        return null;
    }

    @Override
    public HokageServerDO selectById(Long id) {
        return null;
    }

    @Override
    public List<HokageServerDO> selectByType(String type) {
        return null;
    }

    @Override
    public List<HokageServerDO> selectByGroup(String group) {
        return null;
    }
}
