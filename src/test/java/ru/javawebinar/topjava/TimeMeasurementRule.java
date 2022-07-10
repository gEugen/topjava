package ru.javawebinar.topjava;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class TimeMeasurementRule extends Stopwatch {

    private static final Logger log = LoggerFactory.getLogger(TimeMeasurementRule.class);

    private static final String DELIMITERS = String.join("", Collections.nCopies(103, "*"));

    private static final String SPACES = String.join("", Collections.nCopies(87, " "));

    private static final StringBuilder results = new StringBuilder();

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String RED = "\u001B[38;5;1m";

    private static final String CYAN = "\u001B[36m";

    private static final String WHITE = "\u001B[38;5;255m";

    private static final String GREY = "\u001B[38;5;247m";

    private static long totalTime;

    static {
        results.setLength(0);
        results.append("\n" + CYAN)
                .append(DELIMITERS)
                .append("\n")
                .append("Test")
                .append(SPACES)
                .append("Duration, ms\n")
                .append(DELIMITERS)
                .append(ANSI_RESET)
                .append("\n");
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        getAndLogResult(nanos, description, WHITE);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        getAndLogResult(nanos, description, RED);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        getAndLogResult(nanos, description, GREY);
    }

    @Override
    protected void finished(long nanos, Description description) {
        if (description.isSuite()) {
            String result = String.format("%-95s %7d", "Total time", totalTime);
            results.append(CYAN)
                    .append(DELIMITERS)
                    .append("\n")
                    .append(result).append("\n")
                    .append(DELIMITERS)
                    .append(ANSI_RESET);
            log.info(results.toString());
        }
    }

    private void getAndLogResult(long nanos, Description description, String color) {
        if (description.isTest()) {
            long time = TimeUnit.NANOSECONDS.toMillis(nanos);
            totalTime = totalTime + time;
            String result = String.format("%-95s %7d", description.getDisplayName(), time);
            log.info(result + " ms"); // выводит в дефаултном цвете
            results.append(color)
                    .append(result)
                    .append("\n");
        }
    }
}
