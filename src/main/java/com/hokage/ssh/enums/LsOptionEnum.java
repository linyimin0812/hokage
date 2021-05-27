package com.hokage.ssh.enums;

/**
 * @author yiminlin
 * @date 2021/05/27 12:44 上午
 * @description ls command option
 **/
public enum LsOptionEnum {
    /**
     * ls option
     */
    r("reverse order while sorting"),
    S("sort by file size"),
    t("sort by modification time, newest first")

    ;

    LsOptionEnum(String description) {}
}
