package com.easymeeting.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期格式化工具，基于 FastDateFormat 线程安全实现。
 */
public final class DateUtil {

    private static final Map<String, FastDateFormat> FORMAT_CACHE = new ConcurrentHashMap<>();

    private DateUtil() {
    }

    public static String format(Date date, String pattern) {
        if (date == null || StringTools.isEmpty(pattern)) {
            return null;
        }
        return FORMAT_CACHE
                .computeIfAbsent(pattern, FastDateFormat::getInstance)
                .format(date);
    }
}

