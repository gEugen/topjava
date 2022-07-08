package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeMeasurementRule extends Stopwatch {

    private static final Logger log = LoggerFactory.getLogger(TimeMeasurementRule.class);

    private static final String DELIMITERS = String.join("", Collections.nCopies(103, "*"));

    private static final String SPACES = String.join("", Collections.nCopies(87, " "));

    private static final Map<String, Row> results = new LinkedHashMap<>();

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_CYAN = "\u001B[36m";

    public static final String ANSI_WHITE = "\u001B[37m";

    private static long totalTime;

    @Override
    protected void succeeded(long nanos, Description description) {
        if (!"ru.javawebinar.topjava.service.MealServiceTest".equals(description.getDisplayName())) {
            long time = TimeUnit.NANOSECONDS.toMillis(nanos);
            totalTime = totalTime + time;
            String result = String.format("%-95s %7d", description.getDisplayName(), time);
            results.put(description.getDisplayName(), new Row(result, ANSI_WHITE));
            log.info(result + " ms\n");
        }
    }

    @Override
    protected void finished(long nanos, Description description) {
        if (!"ru.javawebinar.topjava.service.MealServiceTest".equals(description.getDisplayName())) {
            if (!results.containsKey(description.getDisplayName())) {
                long time = TimeUnit.NANOSECONDS.toMillis(nanos);
                totalTime = totalTime + time;
                String result = String.format("%-95s %7d", description.getDisplayName(), time);
                results.put(description.getDisplayName(), new Row(result, ANSI_RED));
                log.info(result + " ms\n");
            }
        } else {
            System.out.println(ANSI_CYAN + DELIMITERS + ANSI_RESET);
            System.out.println(ANSI_CYAN + "Test" + SPACES + "Duration, ms" + ANSI_RESET);
            System.out.println(ANSI_CYAN + DELIMITERS + ANSI_RESET);
            results.values().stream().map(row -> row.color + row.text + ANSI_RESET).forEach(System.out::println);
            System.out.println(ANSI_CYAN + DELIMITERS + ANSI_RESET);
            String result = String.format("%-92s %7d", "Total time", totalTime);
            System.out.println(ANSI_CYAN + result + " ms" + ANSI_RESET);
            System.out.println(ANSI_CYAN + DELIMITERS + ANSI_RESET);
            System.out.println();
        }
    }

    private static class Row {
        String text;
        String color;

        private Row(String text, String color) {
            this.text = text;
            this.color = color;
        }
    }
}
