package ru.javawebinar.topjava.optional;

public class MealIdGenerator {
    private static Integer id;
    private static class IdGeneratorHolder {
        public static final MealIdGenerator HOLDER_INSTANCE = new MealIdGenerator();
    }

    private static MealIdGenerator getInstance() {
        return MealIdGenerator.IdGeneratorHolder.HOLDER_INSTANCE;
    }

    public static synchronized Integer getId() {
        if (id == null) {
            getInstance();
            id = 0;
        }
        return ++id;
    }
}
