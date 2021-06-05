package com.hokage.biz.response.file;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yiminlin
 * @date 2021/06/06 3:36 am
 * @description file content vo
 **/
@Data
@Accessors(chain = true)
public class FileContentVO {
    private String name;
    private String type;
    private String content;
}
