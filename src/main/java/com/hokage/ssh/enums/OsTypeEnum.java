package com.hokage.ssh.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author linyimin
 * @date 2021/05/26 08:56 am
 * @email linyimin520812@gmail.com
 * @description os type enum: darwin, linux and unknown
 */
public enum OsTypeEnum {
    /**
     * os type
     */
    darwin,
    linux,
    unknown;

    public static OsTypeEnum parse(String os) {
        return Arrays.stream(OsTypeEnum.values()).filter(type -> StringUtils.equals(type.name(), os)).findFirst().orElse(unknown);
    }

}
