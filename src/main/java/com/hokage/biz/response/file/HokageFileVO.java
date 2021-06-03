package com.hokage.biz.response.file;

import lombok.Data;

/**
 * @author linyimin
 * @date 2021/05/24 20:56 pm
 * @email linyimin520812@gmail.com
 * @description detail of file
 */

@Data
public class HokageFileVO {

    private String typeAndPermission;
    private String owner;
    private String group;
    private Long size;
    private String lastAccessTime;
    private String name;

}
