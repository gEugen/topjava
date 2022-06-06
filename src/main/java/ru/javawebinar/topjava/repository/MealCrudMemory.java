package ru.javawebinar.topjava.repository;

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
    // This is the Memory of CRUD.
    private static Map<LocalDateTime, Meal> storageByDateTime;
    // This is the Meal ID generator.
    private static MealIdGenerator idGenerator;
    // This is the map of requested meals.
    private static final Map<LocalDateTime, Meal> requestedMeals = new HashMap<>();

// ***** The code is left here for future analysis.
//    private static Map<Integer, Meal> storageById;

    private MealCrudMemory() {
            idGenerator = MealIdGenerator.getInstance();
        final List<Meal> initMeals = new ArrayList<>(Arrays.asList(
                new Meal(idGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(idGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(idGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(idGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(idGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(idGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(idGenerator.getId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
        storageByDateTime = new ConcurrentHashMap<>(initMeals.stream().collect(Collectors.toMap(Meal::getDateTime, Function.identity())));
// ***** The code is left here for future analysis.
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
            // Initializes the Meal CRUD Memory.
            getInstance();
        }
        return storageByDateTime;
    }

    public static void addOrUpdate(Meal accumulatorMeal, LocalDateTime dateTime, String description, int calories) {
        Meal mealSource = getMealSource(dateTime, accumulatorMeal, description, calories);
        if (mealSource != null) {
            // Stores in CRUD Memory
            storageByDateTime.put(dateTime, mealSource);
            if (isMealMoved(accumulatorMeal, dateTime)) {
                // Deletes after updating with move.
                storageByDateTime.remove(accumulatorMeal.getDateTime());
            }
            // Removes the meal from requested meals list.
            requestedMeals.remove(mealSource.getDateTime());
        }
    }

    public static void delete(LocalDateTime dateTime) {
        Meal mealSource = getMealSource(dateTime, null, null, 0);
        if (mealSource != null) {
            storageByDateTime.remove(dateTime);
            requestedMeals.remove(mealSource.getDateTime());
        }
    }

    public static Meal get(LocalDateTime dateTime) {
        return getStorageByDateTime().get(dateTime);
    }

    public static List<MealTo> getAll(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return MealsUtil.filteredByStreams(new ArrayList<>(getStorageByDateTime().values()), startTime, endTime, caloriesPerDay);
    }

    public static Meal getDefault() {
        return new Meal(0, LocalDateTime.of(LocalDateTime.now().toLocalDate().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0), "", 0);
    }

    // Provides getting of meal object for working with CRUD memory.
    // Provides holding of the meal for the duration of the operation
    private static synchronized Meal getMealSource(LocalDateTime dateTime, Meal accumulatorMeal, String description, int calories) {
        if (isDateTimeRequested(dateTime)) {
            return null;
        }
        Meal crudMeal = getStorageByDateTime().get(dateTime);

        // Provides delete operation.
        if(crudMeal != null && accumulatorMeal == null && description == null && calories == 0) {
            Meal deleteMealSource = new Meal(crudMeal.getId(), dateTime, crudMeal.getDescription(), crudMeal.getCalories());
            requestedMeals.put(deleteMealSource.getDateTime(), deleteMealSource);
            return deleteMealSource;
        } else if (crudMeal == null && accumulatorMeal == null && description == null && calories == 0) {
            return null;
        }

        // Provides creating operation.
        if (crudMeal == null) {
            Meal newMealSource = new Meal(idGenerator.getId(), dateTime, description, calories);
            requestedMeals.put(newMealSource.getDateTime(), newMealSource);
            return newMealSource;

        } else {
            if (accumulatorMeal == null) {
                return null;

            // Provides updating operation without move.
            } else if (dateTime.equals(accumulatorMeal.getDateTime())) {
                Meal replaceMealSource = new Meal(crudMeal.getId(), dateTime, description, calories);
                requestedMeals.put(replaceMealSource.getDateTime(), replaceMealSource);
                return replaceMealSource;
            }
        }

        return null;
    }

    private static boolean isDateTimeRequested(LocalDateTime dateTime) {
        return requestedMeals.containsKey(dateTime);
    }

    private static boolean isMealMoved(Meal accumulatorMeal, LocalDateTime dateTime) {
        return accumulatorMeal != null && !dateTime.equals(accumulatorMeal.getDateTime());
    }
}
