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
    private static Map<LocalDateTime, Meal> storageByDateTime;
    private static Map<Integer, Meal> storageById;
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
        storageByDateTime = new ConcurrentHashMap<>(initMeals.stream().collect(Collectors.toMap(Meal::getDateTime, Function.identity())));
        storageById = new ConcurrentHashMap<>(initMeals.stream().collect(Collectors.toMap(Meal::getId, Function.identity())));
    }

    private static class CrudMemoryHolder {
        public static final CrudMemory HOLDER_INSTANCE = new CrudMemory();
    }

    private static CrudMemory getInstance() {
        return CrudMemoryHolder.HOLDER_INSTANCE;
    }

    private static Map<LocalDateTime, Meal> getStorageByDateTime() {
        if (storageByDateTime == null && storageById == null) {
            getInstance();
        }

        return storageByDateTime;
    }

    private static Map<Integer, Meal> getStorageById() {
        if (storageByDateTime == null && storageById == null) {
            getInstance();
        }

        return storageById;
    }

    public static void add(LocalDateTime dateTime, String description, int calories) {
        if (getStorageByDateTime().containsKey(dateTime)) {
            Integer id = getStorageByDateTime().get(dateTime).getId();
            Meal meal = new Meal(id, dateTime, description, calories);
            getStorageByDateTime().put(dateTime, meal);
            getStorageById().put(id, meal);

        } else {
            Meal meal = new Meal(IdGenerator.getId(), dateTime, description, calories);
            getStorageByDateTime().put(dateTime, meal);
            getStorageById().put(meal.getId(), meal);
        }
    }

    public static void delete(LocalDateTime dateTime) {
        Integer id = getStorageByDateTime().get(dateTime).getId();
        getStorageById().remove(id);
        getStorageByDateTime().remove(dateTime);
    }

    public static Meal get(LocalDateTime dateTime) {
        return getStorageByDateTime().get(dateTime);
    }

    public static List<MealTo> getAll(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return MealsUtil.filteredByStreams(new ArrayList<>(getStorageByDateTime().values()), startTime, endTime, caloriesPerDay);
    }

    public static Meal getDefault() {
        return new Meal(0, LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0), "Завтрак", 0);
    }
}
