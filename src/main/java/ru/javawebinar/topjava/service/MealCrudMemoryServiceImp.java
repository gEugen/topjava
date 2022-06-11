package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class MealCrudMemoryServiceImp implements MealCrudMemoryService {
    private static final Logger LOG = getLogger(MealCrudMemory.class);
    private final MealCrudMemory mealMemory = MealCrudMemory.getInstance();
    // This is the Meal Memory reference of the CRUD Memory
    // Это ссылка на Память еды CRUD памяти
    private final Map<Integer, Meal> idMealMemory = mealMemory.getMealStorage();
    // This is the Fast Search Memory reference of the CRUD Memory to determine uniqueness and date/time matching
    // Это ссылка на Память быстрого поиска CRUD памяти для определения уникальности и соответствия даты/времени
    private final Map<LocalDateTime, Integer> dateTimeIdFastSearchMemory = mealMemory.getDateTimeStorage();
    private final IdGenerator idGenerator = mealMemory.getIdGenerator();

    @Override
    public void addMeal(Meal newMeal) {
        LOG.debug("adds a meal to the CRUD memory");

        int id = idGenerator.setMealId();
        // Tries to load the pair of the date/time as a key and the generated ID as a value into the Fast Search Memory
        // where stores similar pairs of meals previously loaded into the Meal Memory of CRUD memory
        // Пробует загрузить пару дата/время как ключ и сгенерированный ID как значение в Память быстрого поиска,
        // где хранятся анологичные пары ранее сохранненой еды в Память еды CRUD памяти
        int testId = dateTimeIdFastSearchMemory.merge(newMeal.getDateTime(), id, ((v1, v2) -> v1));
        // Checks the equality of new meal and crud meal IDs after executing the merge method
        // if equals load the new meal in Meal Memory CRUD memory
        // Проверяет равенство ID новой еды и еды из CRUD после выполнения метода merge,
        // если равны, загружает новую еду в Память еды CRUD памяти
        if (testId == id) {
            Meal newCrudMeal = new Meal(id, newMeal.getDateTime(), newMeal.getDescription(), newMeal.getCalories());
            idMealMemory.put(id, newCrudMeal);
            LOG.debug("added a new meal to CRUD memory");

        } else {
            // if not equals, tries to roll back the generated ID to the previous value
            // если не равны, пробует откатить сгенерированный ID к предыдущему значению
            // из-за неудачно завершившейся операции обновления
            idGenerator.rollbackMealId(id);
            LOG.debug("didn't add a new meal with similar date/time");
        }
    }

    @Override
    public void updateMeal(Meal updatedMeal) {
        LOG.debug("updates a meal in the CRUD memory");
        Meal mealMem = idMealMemory.get(updatedMeal.getId());
        int testId = dateTimeIdFastSearchMemory.merge(updatedMeal.getDateTime(), updatedMeal.getId(), ((v1, v2) -> v1));
        // Checks similar date/time and ID pair presence in the Fast Search Memory
        // of the CRUD Memory to carry out the update operation
        // Проверяет наличие подобной пары дата/время и ID в Памяти быстрого поиска
        // CRUD памяти для проведения операции обновления
        if (testId == updatedMeal.getId()) {
            // Checks replace ability and replace
            // Проверяет возможность замещения и замещает
            if (idMealMemory.replace(updatedMeal.getId(), updatedMeal) != null) {
                if (!updatedMeal.getDateTime().equals(mealMem.getDateTime())) {
                    // Deletes date/time and ID pair from the Fast Search Memory
                    // of the CRUD Memory if a meal date/time was changed
                    // Удаляет пару дата/время и ID из Памяти быстрого поиска
                    // CRUD памяти, если дата/время еды была изменена
                    dateTimeIdFastSearchMemory.remove(mealMem.getDateTime());
                }
                LOG.debug("updated a meal in the CRUD memory");

            } else {
                // Deletes saved date/time and ID pair if such meal isn't exist
                // in the Fast Search Memory of the CRUD Memory
                // Удалят сохраненную пару дата/время и ID из Памяти быстрого поиска
                // CRUD памяти, если такая еда отсутсвует в CRUD памяти
                dateTimeIdFastSearchMemory.remove(updatedMeal.getDateTime());
                LOG.debug("did not updated a non-existent meal");
            }
        }
        LOG.debug("didn't updated a meal with a similar date/time of a meal in the CRUD memory");
    }

    @Override
    public Meal getMeal(int mealId) {
        LOG.debug("returns a meal on request from doGet");
        return idMealMemory.get(mealId);
    }

    @Override
    public void deleteMeal(Meal deletedMeal) {
        LOG.debug("deletes a meal from the CRUD memory");
        // Checks if the meal being deleted from the Meal Memory of the CRUD Memory matches the requested one
        // Проверяет, соответствует удаляемая еда из Памяти еди CRUD памяти запрошенной к удалению
        if (idMealMemory.remove(deletedMeal.getId(), deletedMeal)) {
            // Deletes the date/time and ID pair of the deleted meal from the Fast Search Memory of the CRUD Memory
            // if the ID being deleted meal matches the requested one
            // Удаляет пару дата/время и ID удаляемой еды из Памяти быстрого поиска CRUD памяти, если ID
            // удаляемой еды соответствует запрошенной к удалению
            dateTimeIdFastSearchMemory.remove(deletedMeal.getDateTime(), deletedMeal.getId());
            LOG.debug("deleted a meal from the CRUD memory");
        } else {
            LOG.debug("didn't find such meal for delete from the CRUD memory");
        }
    }

    @Override
    public List<Meal> getAllMeals() {
        LOG.debug("returns a list of meals from the CRUD memory");
        return new ArrayList<>(idMealMemory.values());
    }
}
