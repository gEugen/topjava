package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealCrudMemory {
    // This is the Memory of Meal CRUD
    private static Map<Integer, Meal> storageById;
    // This is the Memory of Meal date and time with ID for date and time presence checking
    private static Map<LocalDateTime, Integer> storageDateTimeWithId;
    // This is the Meal ID generator.
    private static IdGenerator idGenerator;
    private static final Logger LOG = getLogger(MealCrudMemory.class);

    private MealCrudMemory() {
        idGenerator = IdGenerator.getInstance();
        final List<Meal> initMeals = new ArrayList<>(Arrays.asList(
                new Meal(idGenerator.setMealId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(idGenerator.setMealId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(idGenerator.setMealId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(idGenerator.setMealId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(idGenerator.setMealId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(idGenerator.setMealId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(idGenerator.setMealId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
        storageById = new ConcurrentHashMap<>(initMeals.stream().collect(Collectors.toMap(Meal::getId, Function.identity())));
        storageDateTimeWithId = new ConcurrentHashMap<>(initMeals.stream().collect(Collectors.toMap(Meal::getDateTime, Meal::getId)));
        LOG.debug("initialized the Meals CRUD memory and Date/Time Pairs map");
    }

    // For CRUD Memory initialisation
    public static MealCrudMemory getInstance() {
        return CrudMemoryHolder.HOLDER_INSTANCE;
    }

    private static class CrudMemoryHolder {
        public static final MealCrudMemory HOLDER_INSTANCE = new MealCrudMemory();
    }

    public Map<Integer, Meal> getMealStorage() {
        return storageById;
    }

    public Map<LocalDateTime, Integer> getDateTimeStorage() {
        return storageDateTimeWithId;
    }

    public void add(Meal newMeal) {
        int id = idGenerator.setMealId();
        int testId = storageDateTimeWithId.merge(newMeal.getDateTime(), id, ((v1, v2) -> v1));
        // Checks the similarity of new meal and crud meal IDs after executing the merge method
        if (testId == id) {
            Meal newCrudMeal = new Meal(id, newMeal.getDateTime(), newMeal.getDescription(), newMeal.getCalories());
            storageById.put(id, newCrudMeal);
            LOG.debug("added a new meal to CRUD memory");

        } else {
            idGenerator.resetMealId();
            LOG.debug("didn't add a new meal with similar date/time");
        }
    }
}
