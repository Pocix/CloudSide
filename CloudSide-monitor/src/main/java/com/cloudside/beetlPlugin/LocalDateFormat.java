package com.cloudside.beetlPlugin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.beetl.core.Format;

public class LocalDateFormat implements Format {

    private Map<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();

    @Override
    public Object format(Object data, String pattern) {
        if (data == null) {
            return null;
        }
        if (!LocalDate.class.isAssignableFrom(data.getClass())) {
            throw new RuntimeException("format failed, expectedClass:" + LocalDate.class
                    + " actualClass:" + data.getClass());
        }
        LocalDate localDate = (LocalDate) data;
        DateTimeFormatter dateTimeFormatter = genDateTimeFormatter(pattern);
        return localDate.format(dateTimeFormatter);
    }

    private DateTimeFormatter genDateTimeFormatter(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return DateTimeFormatter.ISO_LOCAL_DATE;
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
