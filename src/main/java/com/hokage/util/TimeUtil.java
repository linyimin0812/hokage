package com.hokage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author yiminlin
 * @date 2021/06/17 3:19 am
 * @description date and time util
 **/
public class TimeUtil {

    public static final String PROCESS_TIME_FORMAT = "EEE MMM d HH:mm:ss yyyy";
    public static final String MONITOR_TIME_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String DISPLAY_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String METRIC_FORMAT = "yyyy-MM-dd HH:mm";

    public static String format(String dateStr, String srcFormat, String dstFormat) throws ParseException {
        SimpleDateFormat srcFormatter = new SimpleDateFormat(srcFormat, Locale.ENGLISH);
        long timestamp = srcFormatter.parse(dateStr).getTime();
        return format(timestamp, dstFormat);
    }

    public static String format(Long timestamp, String format) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter dstFormatter = DateTimeFormatter.ofPattern(format);
        return zonedDateTime.format(dstFormatter);
    }

    public static Long timestamp(String dateStr, String format) throws ParseException {
        SimpleDateFormat srcFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return srcFormatter.parse(dateStr).getTime();
    }
}
