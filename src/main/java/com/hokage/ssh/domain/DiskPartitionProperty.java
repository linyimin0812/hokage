package com.hokage.ssh.domain;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/20 6:08 pm
 * @description disk partition property
 **/
@Data
public class DiskPartitionProperty {
    private String fileSystem;
    private Long size;
    private Long used;
    private Long available;
    private String capacity;
    private String mounted;
}
