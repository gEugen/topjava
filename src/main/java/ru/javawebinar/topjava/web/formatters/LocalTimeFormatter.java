package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public LocalTime parse(String localTimeText, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalTime(localTimeText);
    }
}