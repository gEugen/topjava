package ru.javawebinar.topjava;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class TimeMeasurementRules {

    private static final Logger log = LoggerFactory.getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    public static final Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-95s %7d", description.getDisplayName(),
                    TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };

    private static final String DELIMITERS = String.join("", Collections.nCopies(103, "*"));

    private static final String SPACES = String.join("", Collections.nCopies(87, " "));

    public static final ExternalResource SUMMARY = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            results.setLength(0);
        }

        @Override
        protected void after() {
            log.info("\n" + DELIMITERS + "\nTest" + SPACES + "Duration, ms" + "\n" + DELIMITERS + "\n" + results + DELIMITERS + "\n");
        }
    };
}
