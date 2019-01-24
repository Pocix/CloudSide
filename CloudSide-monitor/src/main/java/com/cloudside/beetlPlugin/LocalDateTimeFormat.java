package com.cloudside.beetlPlugin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.beetl.core.Format;

public class LocalDateTimeFormat implements Format {

    private Map<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();

    @Override
    public Object format(Object data, String pattern) {
        if (data == null) {
            return null;
        }
        if (!LocalDateTime.class.isAssignableFrom(data.getClass())) {
            throw new RuntimeException("format failed, expectedClass:" + LocalDateTime.class
                    + " actualClass:" + data.getClass());
        }
        LocalDateTime localDateTime = (LocalDateTime) data;
        DateTimeFormatter dateTimeFormatter = genDateTimeFormatter(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    private DateTimeFormatter genDateTimeFormatter(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        }

        DateTimeFormatter formatter = formatterMap.get(pattern);
        if (formatter != null) {
            return formatter;
        }
        formatter = DateTimeFormatter.ofPattern(pattern);
        formatterMap.put(pattern, formatter);
        return formatter;
    }
}