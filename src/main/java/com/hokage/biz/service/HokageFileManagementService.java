package com.hokage.biz.service;

import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.enums.LsOptionEnum;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/03 11:44 pm
 * @description file management service
 **/
public interface HokageFileManagementService {
    /**
     * list directory contens
     * @param serverKey ip_sshPort_account
     * @param dir directory path
     * @param options ls command options
     * @return directory content list
     */
    ServiceResponse<List<HokageFileVO>> list(String serverKey, String dir, List<LsOptionEnum> options);
}
