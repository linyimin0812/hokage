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
 * @date 2021/06/17 3:19 上午
 * @description date and time util
 **/
public class TimeUtil {
    public static String format(String dateStr, String srcFormat, String dstFormat) throws ParseException {
        SimpleDateFormat srcFormatter = new SimpleDateFormat(srcFormat, Locale.ENGLISH);
        long timestamp = srcFormatter.parse(dateStr).getTime();
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter dstFormatter = DateTimeFormatter.ofPattern(dstFormat);
        return zonedDateTime.format(dstFormatter);
    }
}
