package ru.javawebinar.topjava.optional;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CrudMemory {
    private static Map<Integer, Meal> storage;
    private CrudMemory() {
        final List<Meal> initMeals = new ArrayList<>(Arrays.asList(
                new Meal(IdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(IdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(IdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(IdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(IdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(IdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(IdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
        storage = initMeals.stream().collect(Collectors.toMap(Meal::getId, Function.identity()));
    }

    private static class CrudMemoryHolder {
        public static final CrudMemory HOLDER_INSTANCE = new CrudMemory();
    }

    public static CrudMemory getInstance() {
        return CrudMemoryHolder.HOLDER_INSTANCE;
    }

    public static Map<Integer, Meal> getStorage() {
        if (storage == null) {
            getInstance();
        }

        return storage;
    }
//    public static synchronized List<Meal> getAllMeals() {
//        return new ArrayList<>(getStorage().values());
//    }
//
//    public static synchronized void saveMeal (Integer id, Meal meal) {
//        storage.put(id, meal);
//    }
//
//    public static synchronized void deleteMeal (Integer id) {
//        storage.remove(id);
//    }
//
//    public static synchronized Meal getMeal (Integer id) {
//        return storage.get(id);
//    }
}
