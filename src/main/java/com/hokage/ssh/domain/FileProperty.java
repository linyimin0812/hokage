package com.hokage.ssh.domain;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/05/27 1:28 am
 * @description file property
 **/
@Data
public class FileProperty {
    /**
     * file type and permission: dr-xr-x---
     */
    private String typeAndPermission;
    private String owner;
    private String group;
    private Long size;
    private String lastAccessTime;
    private String name;
}
