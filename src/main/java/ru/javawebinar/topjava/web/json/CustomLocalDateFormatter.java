package ru.javawebinar.topjava.web.json;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomLocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public LocalDate parse(String localDateText, Locale locale) throws ParseException {
        return LocalDate.parse(localDateText, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}