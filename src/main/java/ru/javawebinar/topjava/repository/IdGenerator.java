package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

class IdGenerator {
    private static class IdGeneratorHolder {
        public static final IdGenerator HOLDER_INSTANCE = new IdGenerator();
    }
    private static final Logger log = getLogger(MealCrud.class);
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
        log.debug("increments the ID counter by one");
        return mealId.addAndGet(1);
    }

    // Tries to roll back the generated ID to the previous value
    // Пробует откатить счетчик назад при условии, что ожидаемое текущее значение равно заданному
    private void rollback(int id) {
        log.debug("tries to decrement the ID counter by one");
        if (mealId.compareAndSet(id, --id)) {
            log.debug("the ID counter was decremented by one");
        } else {
            log.debug("didn't rollback the ID Counter to the old value, the counter was changed by another thread");
        }
    }
}
