package ru.javawebinar.topjava.service;

import org.slf4j.Logger;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

class IdGenerator {
    private static class IdGeneratorHolder {
        public static final IdGenerator HOLDER_INSTANCE = new IdGenerator();
    }
    private static final Logger LOG = getLogger(MealCrudMemory.class);
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
        LOG.debug("increments the ID counter by one at " + LocalTime.now());
        return mealId.addAndGet(1);
    }

    // Tries to roll back the generated ID to the previous value
    // Пробует откатить счетчик назад при условии, что ожидаемое текущее значение равно заданному
    private void rollback(int id) {
        LOG.debug("tries to decrement the ID counter by one at " + LocalTime.now());
        if (mealId.compareAndSet(id, --id)) {
            LOG.debug("the ID counter was decremented by one at " + LocalTime.now());
        } else {
            LOG.debug("didn't rollback the ID Counter to the old value at "  + LocalTime.now() + ", the counter was changed by another thread");
        }
    }
}
