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
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealCrudInMemory implements MealCrud {
    private static final Logger log = getLogger(MealCrudInMemory.class);
    private static final AtomicInteger crudId = new AtomicInteger(0);
    private static final Map<Integer, Meal> storageById;

    static {
        log.debug("initializes the storage");
        final List<Meal> initMeals = new ArrayList<>(Arrays.asList(
                new Meal(crudId.incrementAndGet(),
                        LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(crudId.incrementAndGet(),
                        LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(crudId.incrementAndGet(),
                        LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(crudId.incrementAndGet(),
                        LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(crudId.incrementAndGet(),
                        LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(crudId.incrementAndGet(),
                        LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(crudId.incrementAndGet(),
                        LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
        storageById = new ConcurrentHashMap<>(initMeals.stream()
                .collect(Collectors.toMap(Meal::getId, Function.identity())));
        log.debug("initialized the storage");
    }

    @Override
    public Meal add(Meal meal) {
        log.debug("adds a meal to the storage");
        int id = crudId.incrementAndGet();
        return storageById.put(id, new Meal(id,
                meal.getDateTime(), meal.getDescription(), meal.getCalories()));
    }

    @Override
    public Meal update(Meal meal) {
        log.debug("updates a meal in the storage");
        return storageById.replace(meal.getId(), meal);
    }

    @Override
    public Meal get(int id) {
        log.debug("returns a meal on request from doGet");
        return storageById.get(id);
    }

    @Override
    public void delete(int id) {
        log.debug("deletes a meal from the storage");
        storageById.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("returns a list of meals from the storage");
        return new ArrayList<>(storageById.values());
    }
}
