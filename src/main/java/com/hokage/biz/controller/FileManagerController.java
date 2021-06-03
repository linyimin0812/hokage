package com.hokage.biz.controller;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.form.file.FileOperateForm;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.command.CommandResult;
import com.hokage.ssh.component.SshExecComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/8/23 1:51
 * @email linyimin520812@gmail.com
 * @description
 */
@RestController
public class FileManagerController extends BaseController {

    private HokageServerCacheDao serverCacheDao;

    private CommandDispatcher dispatcher;

    private SshExecComponent execComponent;

    @Autowired
    private void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        this.serverCacheDao = serverCacheDao;
    }

    @Autowired
    private void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    private void setExecComponent(SshExecComponent component) {
        this.execComponent = component;
    }

    @RequestMapping(value = "/server/file/list", method = RequestMethod.POST)
    public ResultVO<List<HokageFileVO>> searchServer(@RequestBody FileOperateForm form) throws Exception {

        String cacheKey = serverCacheDao.buildKey(form.getIp(), form.getSshPort(), form.getAccount());
        Optional<SshClient> optional = serverCacheDao.get(cacheKey);

        if (!optional.isPresent()) {
            return fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }

        SshClient client = optional.get();
        AbstractCommand command = dispatcher.dispatch(client);
        CommandResult result = execComponent.execute(client, command.ls(form.getCurDir(), Collections.emptyList()));

        if (!result.isSuccess()) {
            return fail(String.valueOf(result.getExitStatus()), result.getMsg());
        }

        List<HokageFileVO> list = JSON.parseArray(result.getContent(), HokageFileVO.class);

        return success(list);
    }
}
