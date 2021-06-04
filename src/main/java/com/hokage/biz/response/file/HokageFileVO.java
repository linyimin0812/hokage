package com.hokage.biz.response.file;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author linyimin
 * @date 2021/05/24 20:56 pm
 * @email linyimin520812@gmail.com
 * @description detail of file
 */

@Data
@Accessors(chain = true)
public class HokageFileVO {
    private String curDir;
    private List<HokageFileProperty> filePropertyList;
    private Long directoryNum;
    private Long fileNum;
    private String totalSize;
}
