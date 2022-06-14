package ru.javawebinar.topjava.crud;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealCrud implements MealCrud {
    private static final Logger log = getLogger(InMemoryMealCrud.class);
    private final AtomicInteger crudId;
    private final Map<Integer, Meal> storage;

    public InMemoryMealCrud() {
        log.debug("initializes the meal storage");
        crudId = new AtomicInteger();
        storage = new ConcurrentHashMap<>();
        List<Meal> initMeals = Arrays.asList(
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        initMeals.forEach(this::add);
        log.debug("initialized the meal storage");
    }

    @Override
    public Meal add(Meal meal) {
        log.debug("adds a new meal to the storage");
        int id = crudId.incrementAndGet();
        Meal newMeal = new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        storage.put(id, newMeal);
        return get(id);
    }

    @Override
    public Meal update(Meal meal) {
        log.debug("updates a meal in the storage");
        return storage.replace(meal.getId(), meal) == null ? null : meal;
    }

    @Override
    public Meal get(int id) {
        log.debug("returns a meal from the storage");
        return storage.get(id);
    }

    @Override
    public void delete(int id) {
        log.debug("deletes a meal from the storage");
        storage.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("returns a list of meals from the storage");
        return new ArrayList<>(storage.values());
    }
}
