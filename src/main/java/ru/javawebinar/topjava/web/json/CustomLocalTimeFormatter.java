package ru.javawebinar.topjava.web.json;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomLocalTimeFormatter implements Formatter<LocalTime> {

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public LocalTime parse(String localTimeText, Locale locale) throws ParseException {
        return LocalTime.parse(localTimeText, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}