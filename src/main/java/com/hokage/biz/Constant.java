package com.hokage.biz;

/**
 * @author linyimin
 * @date 2020/9/6 10:53 am
 * @email linyimin520812@gmail.com
 * @description common constant variable
 */
public class Constant {
    /**
     * session key
     */
    public static final String USER_SESSION_KEY = "userInfo";
    /**
     * login page
     */
    public static final String LOGIN_PAGE_URL = "/app/login";
    /**
     * index page
     */
    public static final String INDEX_URL = "/app/index";

    /**
     * hokage work home directory (use in server, managed by ssh)
     */
    public static final String WORK_HOME = ".hokage";
    /**
     * Linux shell script path
     */
    public static final String LINUX_API_FILE = "/shell/linux-json-api.sh";
    /**
     * darwin shell script path
     */
    public static final String DARWIN_API_FILE = "/shell/darwin-json-api.sh";

    /**
     * API file name
     */
    public static final String API_FILE = "json-api.sh";
}
