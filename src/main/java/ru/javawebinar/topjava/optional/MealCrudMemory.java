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

public class MealCrudMemory {
    private static Map<LocalDateTime, Meal> storageByDateTime;
//    private static Map<Integer, Meal> storageById;
    private MealCrudMemory() {
        final List<Meal> initMeals = new ArrayList<>(Arrays.asList(
                new Meal(MealIdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(MealIdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(MealIdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(MealIdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(MealIdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(MealIdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(MealIdGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
        storageByDateTime = new ConcurrentHashMap<>(initMeals.stream().collect(Collectors.toMap(Meal::getDateTime, Function.identity())));
//        storageById = new ConcurrentHashMap<>(initMeals.stream().collect(Collectors.toMap(Meal::getId, Function.identity())));
    }

    private static class CrudMemoryHolder {
        public static final MealCrudMemory HOLDER_INSTANCE = new MealCrudMemory();
    }

    // For CRUD Memory initialisation.
    private static MealCrudMemory getInstance() {
        return CrudMemoryHolder.HOLDER_INSTANCE;
    }

    private static Map<LocalDateTime, Meal> getStorageByDateTime() {
        if (storageByDateTime == null) {
//        if (storageByDateTime == null && storageById == null) {
            getInstance();
        }

        return storageByDateTime;
    }

//    private static Map<Integer, Meal> getStorageById() {
//        if (storageByDateTime == null && storageById == null) {
//            getInstance();
//        }
//
//        return storageById;
//    }

    public static void add(Meal mealAccumulator, LocalDateTime dateTime, String description, int calories) {
        Meal meal = getCopy(dateTime);
        if (meal != null || mealAccumulator != null) {
            Integer id;
            if (meal != null) {
                id = meal.getId();
                meal = new Meal(id, dateTime, description, calories);

            } else {
                id = mealAccumulator.getId();
                meal = new Meal(id, dateTime, description, calories);
                delete(mealAccumulator.getDateTime());
            }

            storageByDateTime.put(dateTime, meal);
//            storageById.put(id, meal);

        } else {
            meal = new Meal(MealIdGenerator.getId(), dateTime, description, calories);
            storageByDateTime.put(dateTime, meal);
//            storageById.put(meal.getId(), meal);
        }
    }

    public static void delete(LocalDateTime dateTime) {
        Meal meal = getCopy(dateTime);
        if (meal != null) {
//            Integer id = meal.getId();
//            storageById.remove(id);
            storageByDateTime.remove(dateTime);
        }
    }

    public static Meal get(LocalDateTime dateTime) {
        return getCopy(dateTime);
    }

    public static List<MealTo> getAll(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return MealsUtil.filteredByStreams(new ArrayList<>(getStorageByDateTime().values()), startTime, endTime, caloriesPerDay);
    }

    public static Meal getDefault() {
        return new Meal(0, LocalDateTime.of(LocalDateTime.now().toLocalDate().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0), "", 0);
    }

    private static synchronized Meal getCopy(LocalDateTime dateTime) {
        Meal meal = getStorageByDateTime().get(dateTime);
        if (meal == null) {
            return null;
        }
        return new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }
}
