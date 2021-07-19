package com.hokage.persistence.dao.impl;

import com.hokage.biz.request.server.AllServerQuery;
import com.hokage.biz.request.server.SubordinateServerQuery;
import com.hokage.biz.request.server.SupervisorServerQuery;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dao.HokageSubordinateServerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.persistence.mapper.HokageServerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description server info dao interface implementation
 */
@Repository
public class HokageServerDaoImpl implements HokageServerDao {

    private HokageServerMapper serverMapper;
    private HokageSubordinateServerDao subordinateServerDao;

    @Autowired
    public void setServerMapper(HokageServerMapper serverMapper) {
        this.serverMapper = serverMapper;
    }

    @Autowired
    public void setSubordinateServerDao(HokageSubordinateServerDao subordinateServerDao) {
        this.subordinateServerDao = subordinateServerDao;
    }

    @Override
    public Long insert(HokageServerDO serverDO) {
        return serverMapper.insert(serverDO);
    }

    @Override
    public Long update(HokageServerDO serverDO) {
        return serverMapper.update(serverDO);
    }

    @Override
    public List<HokageServerDO> selectAll() {
        return Optional.ofNullable(serverMapper.selectAll()).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageServerDO> selectByIds(List<Long> ids) {
        return Optional.ofNullable(serverMapper.selectByIds(ids)).orElse(Collections.emptyList());
    }

    @Override
    public HokageServerDO selectById(Long id) {
        return serverMapper.selectById(id);
    }


    @Override
    public List<HokageServerDO> listByType(String type) {
        return Collections.emptyList();
    }


    @Override
    public List<HokageServerDO> selectByGroup(String group) {
        return Optional.ofNullable(serverMapper.selectByGroup(group)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageServerDO> selectBySupervisorId(Long supervisorId) {
        return Optional.ofNullable(serverMapper.selectBySupervisorId(supervisorId)).orElse(Collections.emptyList());
    }

    @Override
    public HokageServerDO selectByAccount(String ip, String sshPort, String account) {
        return serverMapper.selectByAccount(ip, sshPort, account);
    }

    @Override
    public List<HokageServerDO> selectByAllQuery(AllServerQuery query) {
        return Optional.ofNullable(serverMapper.selectByAllServerQuery(query)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageServerDO> selectBySupervisorQuery(SupervisorServerQuery query) {
        return Optional.ofNullable(serverMapper.selectBySupervisorServerQuery(query)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageServerDO> selectByUserId(Long userId) {
        return Optional.ofNullable(serverMapper.selectByUserId(userId)).orElse(Collections.emptyList());
    }

    @Override
    public Long deleteById(Long id) {
        return serverMapper.deleteById(id);
    }

    @Override
    public List<HokageServerDO> selectSubordinateServer(SubordinateServerQuery query) {
        return Optional.ofNullable(serverMapper.selectBySubordinateServerQuery(query)).orElse(Collections.emptyList());
    }

    @Override
    public Optional<HokageServerDO> selectByIdAndAccount(long serverId, String account) {
        Optional<HokageServerDO> optional;
        HokageServerDO serverDO = serverMapper.selectById(serverId);
        if (StringUtils.equals(serverDO.getAccount(), account)) {
            optional = Optional.of(serverDO);
        } else {
            List<HokageSubordinateServerDO> subServerDOList = subordinateServerDao.listByServerIds(Collections.singletonList(serverId));
            optional = subServerDOList.stream()
                    .filter(subServerDO -> StringUtils.equals(account, subServerDO.getAccount()))
                    .map(subServerDO -> {
                        serverDO.setAccount(subServerDO.getAccount());
                        serverDO.setPasswd(subServerDO.getPasswd());
                        serverDO.setLoginType(subServerDO.getLoginType());
                        return serverDO;
                    })
                    .findFirst();
        }
        return optional;
    }
}
