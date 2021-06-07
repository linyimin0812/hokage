package com.hokage.biz.response.file;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/04 10:18 pm
 * @description web file property
 **/
@Data
public class HokageFileProperty {
    private String name;
    private String type;
    private String permission;
    private String owner;
    private String group;
    private String size;
    private String lastAccessTime;
    private String curDir;
}
