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
    private static final Map<Integer, Meal> requestedMeals = new HashMap<>();

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
        Meal crudMeal = getCopy(dateTime);
        Integer requestedMealId = getRequestedMealId(crudMeal);

        // Updates the CRUD Memory without moving.
        if (isMealValidToUpdate(crudMeal, accumulatorMeal)) {
            crudMeal = new Meal(crudMeal.getId(), dateTime, description, calories);

        // Updates the CRUD Memory with move.
        } else if (isMealValidToUpdateWithMove(crudMeal, accumulatorMeal)) {
            crudMeal = new Meal(idGenerator.getId(), dateTime, description, calories);
            delete(accumulatorMeal.getDateTime());

        // Creates a new meal in the CRUD Memory.
        } else if (isMealValidToCreate(crudMeal, accumulatorMeal)) {
            crudMeal = new Meal(idGenerator.getId(), dateTime, description, calories);

        } else {
            return;
        }

        storageByDateTime.put(dateTime, crudMeal);
        if (requestedMealId != null) {
            requestedMeals.remove(requestedMealId);
        }
    }

    public static void delete(LocalDateTime dateTime) {
        Meal meal = getCopy(dateTime);
        if (meal != null) {
            storageByDateTime.remove(dateTime);
            requestedMeals.remove(meal.getId());
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

    // Provides getting of existing meal object in the CRUD memory.
    // Provides holding of the meal for the duration of the operation
    private static synchronized Meal getCopy(LocalDateTime dateTime) {
        Meal crudMeal = getStorageByDateTime().get(dateTime);
        if (crudMeal == null || isMealRequested(crudMeal)) {
            return null;
        }
        requestedMeals.put(crudMeal.getId(), crudMeal);

        return new Meal(crudMeal.getId(), crudMeal.getDateTime(), crudMeal.getDescription(), crudMeal.getCalories());
    }

    private static boolean isMealValidToUpdate(Meal crudMeal, Meal accumulatorMeal) {
        return crudMeal != null && accumulatorMeal != null && crudMeal.getId().equals(accumulatorMeal.getId());
    }

    private static boolean isMealValidToUpdateWithMove(Meal crudMeal, Meal accumulatorMeal) {
        return crudMeal == null && accumulatorMeal != null;
    }

    private static boolean isMealValidToCreate(Meal crudMeal, Meal accumulatorMeal) {
        return crudMeal == null && accumulatorMeal == null;
    }

    private static boolean isMealRequested(Meal testMeal) {
        return requestedMeals.containsKey(testMeal.getId());
    }

    private static Integer getRequestedMealId(Meal crudMeal) {
        if (crudMeal != null) {
            return crudMeal.getId();
        }
        return null;
    }
}
