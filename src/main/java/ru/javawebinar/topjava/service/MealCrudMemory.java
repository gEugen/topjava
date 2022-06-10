package ru.javawebinar.topjava.service;

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
    // Это CRUD память еды
    private static Map<Integer, Meal> storageById;
    // This is the Memory of Meal date and time with ID for date and time presence checking
    // Это память пар дата/время и ID для проверки совпадения даты/времены
    // обрабатываемого объекта с датой временем объекта находящемся в CRUD памяти
    private static Map<LocalDateTime, Integer> storageDateTimeWithId;
    // This is the Meal ID generator
    // Это генератор ID
    private static IdGenerator idGenerator;
    private static final Logger LOG = getLogger(MealCrudMemory.class);

    private static class CrudMemoryHolder {
        public static final MealCrudMemory HOLDER_INSTANCE = new MealCrudMemory();
    }

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

    // Initializes CRUD Memory
    // Инициализирует CRUD память
    static MealCrudMemory getInstance() {
        return CrudMemoryHolder.HOLDER_INSTANCE;
    }

    Map<Integer, Meal> getMealStorage() {
        return storageById;
    }

    Map<LocalDateTime, Integer> getDateTimeStorage() {
        return storageDateTimeWithId;
    }

    void add(Meal newMeal) {
        int id = idGenerator.setMealId();
        // Tries to load the pair of the date/time as a key and the generated ID as a value
        // into the map where stores similar pairs of meals previously loaded into CRUD memory
        // Пробует загрузить пару дата/время как ключ и сгенерированный ID как значение в map,
        // где хранятся анологичные пары ранее сохранненой еды в памяти CRUD
        int testId = storageDateTimeWithId.merge(newMeal.getDateTime(), id, ((v1, v2) -> v1));
        // Checks the equality of new meal and crud meal IDs after executing the merge method
        // if equals load the new meal in CRUD memory
        // Проверяет равенство ID новой еды и еды из CRUD после выполнения метода merge,
        // если равны, загружает новую еду в память CRUD
        if (testId == id) {
            Meal newCrudMeal = new Meal(id, newMeal.getDateTime(), newMeal.getDescription(), newMeal.getCalories());
            storageById.put(id, newCrudMeal);
            LOG.debug("added a new meal to CRUD memory");

        } else {
            // if not equals, tries to roll back the generated ID to the previous value
            // если не равны, пробует откатить сгенерированный ID к предыдущему значению
            // из-за неудачно завершившейся операции обновления
            idGenerator.rollbackMealId(id);
            LOG.debug("didn't add a new meal with similar date/time");
        }
    }
}
