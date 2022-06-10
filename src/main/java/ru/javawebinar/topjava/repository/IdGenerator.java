package ru.javawebinar.topjava.repository;

import java.util.concurrent.atomic.AtomicInteger;

class IdGenerator {
    private static class IdGeneratorHolder {
        public static final IdGenerator HOLDER_INSTANCE = new IdGenerator();
    }
    private final AtomicInteger mealId;

    private IdGenerator() {
        mealId = new AtomicInteger(0);
    }

    static IdGenerator getInstance() {
        return IdGenerator.IdGeneratorHolder.HOLDER_INSTANCE;
    }

    int setMealId() {
        return changeId();
    }

    void resetMealId() {
        rollback();
    }

    private int changeId() {
        return mealId.addAndGet(1);
    }

    private void rollback() {
        mealId.decrementAndGet();
    }
}
