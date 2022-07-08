package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CombinedRule implements TestRule {

    private static final Logger log = LoggerFactory.getLogger(CombinedRule.class);

    private static final String DELIMITERS = String.join("", Collections.nCopies(103, "*"));

    private static final String SPACES = String.join("", Collections.nCopies(87, " "));

    private static final List<Row> rows = new ArrayList<>();

    private static long startTime;

    private static long timeBefore;

    private static long timeAfter;

    private static Description underTestMethod;

    public Statement apply(Statement base, Description description) {
        if (description.isTest()) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    before();
                    try {
                        underTestMethod = description;
                        base.evaluate();
                        verify();
                    } finally {
                        after();
                    }
                }
            };
        }
        if (description.isSuite()) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    beforeClass();
                    try {
                        base.evaluate();
                        verifyClass();
                    } finally {
                        afterClass();
                    }
                }
            };
        }
        return base;
    }

    void before() throws Throwable {
        timeBefore = System.currentTimeMillis();
    }

    void after() {
        String result;
        if (timeAfter - timeBefore > 0) {
            result = String.format("%-95s %7d", underTestMethod.getDisplayName(), timeAfter - timeBefore);
            rows.add(new Row(result, false));
        } else {
            result = String.format("%-96s failed", underTestMethod.getDisplayName());
            rows.add(new Row(result, true));
        }
        log.info(result + " ms\n");
    }

    void verify() {
        timeAfter = System.currentTimeMillis();
    }

    void beforeClass() throws Throwable {
        startTime = System.currentTimeMillis();
        rows.add(new Row(DELIMITERS, false));
        rows.add(new Row(("Test" + SPACES + "Duration, ms"), false));
        rows.add(new Row(DELIMITERS, false));
    }

    void afterClass() {
        String result = String.format("%-92s %7d", "Total time", timeBefore - startTime >= 0 ? timeBefore - startTime : 0);
        rows.add(new Row(DELIMITERS, false));
        rows.add(new Row(result + " ms", false));
        rows.add(new Row(DELIMITERS, false));

        rows.forEach(row -> {
            if (row.color) {
                System.out.println("\u001B[31m" + row.text + "\u001B[0m");
            } else {
                System.out.println("\u001B[32m" + row.text + "\u001B[0m");
            }
        });
    }

    void verifyClass() {
        timeAfter = System.currentTimeMillis();
    }

    private static class Row {
        String text;
        boolean color;

        private Row(String text, boolean color) {
            this.text = text;
            this.color = color;
        }
    }
}
