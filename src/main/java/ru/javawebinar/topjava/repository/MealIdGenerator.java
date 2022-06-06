package ru.javawebinar.topjava.repository;

public class MealIdGenerator {
    private Integer id;

    private MealIdGenerator() {
        id = 0;
    }

    private static class IdGeneratorHolder {
        public static final MealIdGenerator HOLDER_INSTANCE = new MealIdGenerator();
    }

    public static MealIdGenerator getInstance() {
        return MealIdGenerator.IdGeneratorHolder.HOLDER_INSTANCE;
    }

    public synchronized Integer getId() {
        return ++id;
    }
}
