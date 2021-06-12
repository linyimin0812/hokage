package com.hokage.biz.service;

import com.hokage.biz.response.file.FileContentVO;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.enums.LsOptionEnum;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    ServiceResponse<HokageFileVO> list(String serverKey, String dir, List<LsOptionEnum> options);

    /**
     * open a file
     * @param serverKey: ip_sshPort_account
     * @param curDir: opened file path
     * @return file content vo
     */
    ServiceResponse<FileContentVO> open(String serverKey, String curDir);

    /**
     * remove file or directory
     * @param serverKey ip_sshPort_account
     * @param curDir file or directory path
     * @return true if remove success otherwise false
     */
    ServiceResponse<Boolean> rm(String serverKey, String curDir);

    /**
     * download file
     * @param id server primary key id
     * @param file file path which is downloaded
     * @param os short for output stream
     * @return  download success - true, otherwise false
     *  @throws IOException io exception
     */
    ServiceResponse<Boolean> download(Long id, String file, OutputStream os) throws IOException;

    /**
     * package folder
     * @param serverKey ip_sshPort_account
     * @param path directory path
     * @return package success is true, otherwise false
     */
    ServiceResponse<Boolean> tar(String serverKey, String path);

    /**
     * upload a file to server
     * @param id server primary key id
     * @param dst upload destination path
     * @param src upload file input stream
     * @return true - upload success otherwise false
     */
    ServiceResponse<Boolean> upload(Long id, String dst, InputStream src);
}
