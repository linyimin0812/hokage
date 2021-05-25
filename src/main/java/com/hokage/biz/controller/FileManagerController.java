package com.hokage.biz.controller;

import com.hokage.biz.form.file.FileOperateForm;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 1:51
 * @email linyimin520812@gmail.com
 * @description
 */
@RestController
public class FileManagerController extends BaseController {
    @RequestMapping(value = "/server/file/list", method = RequestMethod.POST)
    public ResultVO<List<HokageFileVO>> searchServer(@RequestBody FileOperateForm form) {
        return success(Collections.emptyList());
    }
}
