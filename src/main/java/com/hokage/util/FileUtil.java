package com.hokage.util;

/**
 * @author yiminlin
 * @date 2021/06/04 10:26 下午
 * @description file util class
 **/
public class FileUtil {
    private static final double K = 2014;
    private static final double M = K * K;
    private static final double G = M * K;

    public static String humanReadable(long size) {
        if (size < K) {
            return String.format("%.2f B", (double) size);
        }
        if (size < M) {
            return String.format("%.2f KB", size / K);
        }
        if (size < G) {
            return String.format("%.2f MB", size / M);
        }
        return String.format("%.2f GB", size / G);
    }
}
