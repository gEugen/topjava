package ru.javawebinar.topjava.service;

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

    void rollbackMealId(int id) {
        rollback(id);
    }

    private int changeId() {
        return mealId.addAndGet(1);
    }

    // Tries to roll back the generated ID to the previous value
    // Пробует откатить счетчик назад при условии, что ожидаемое текущее значение равно заданному
    private void rollback(int id) {
        mealId.compareAndSet(id, --id);
    }
}
