package ru.javawebinar.topjava.optional;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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

    private static CrudMemory getInstance() {
        return CrudMemoryHolder.HOLDER_INSTANCE;
    }

    private static Map<Integer, Meal> getStorage() {
        if (storage == null) {
            getInstance();
        }

        return storage;
    }

    public static void add(Integer id, Meal meal) {
        getStorage().put(id, meal);
    }

    public static void delete(Integer id) {
        getStorage().remove(id);
    }

    public static Meal get(Integer id) {
        return getStorage().get(id);
    }

    public static List<MealTo> getAll(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return MealsUtil.filteredByStreams(new ArrayList<>(getStorage().values()), startTime, endTime, caloriesPerDay);
    }

    public static Meal getDefault() {
        return new Meal(0, LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0), "Завтрак", 0);
    }
}
