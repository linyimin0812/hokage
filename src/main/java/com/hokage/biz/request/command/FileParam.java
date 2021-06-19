package com.hokage.biz.request.command;

import com.hokage.ssh.SshClient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yiminlin
 * @date 2021/06/19 7:28 pm
 * @description file command parameter
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class FileParam extends BaseCommandParam {
    private String path;
    /**
     * preview page
     */
    private Long page;
    /**
     * for download
     */
    private OutputStream os;
    /**
     * for upload
     */
    private InputStream src;

    /**
     * for move
     */
    private String srcPath;
    private String dstPath;
    /**
     * for chmod
     */
    private String permission;
}
