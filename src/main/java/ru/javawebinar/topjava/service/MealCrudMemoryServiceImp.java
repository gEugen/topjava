package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class MealCrudMemoryServiceImp implements MealCrudMemoryService {
    private static final Logger LOG = getLogger(MealCrudMemory.class);
    private final MealCrudMemory mealMemory = MealCrudMemory.getInstance();
    private final Map<Integer, Meal> mealMap = mealMemory.getMealStorage();
    private final Map<LocalDateTime, Integer> dateTimeWithIdMap = mealMemory.getDateTimeStorage();

    @Override
    public void add(Meal newMeal) {
        LOG.debug("adds a meal to the CRUD memory");
        // Adds new meal with new ID generation to the CRUD Memory
        // Добавляет новую еду с генерацией нового ID в CRUD память
        mealMemory.add(newMeal);
    }

    @Override
    public void update(Meal updatedMeal) {
        int testId = dateTimeWithIdMap.merge(updatedMeal.getDateTime(), updatedMeal.getId(), ((v1, v2) -> v1));
        // Checks similar date/time and ID pair presence to carry out the update operation
        // Проверяет наличие подобной пары дата/время и ID для проведения операции обновления
        if (testId == updatedMeal.getId()) {
            // Checks replace ability and replace
            // Проверяет возможность замещения и замещает
            if (mealMap.replace(updatedMeal.getId(), updatedMeal) != null) {
                LOG.debug("updated a meal in the CRUD memory");

            } else {
                // Deletes saved date/time and ID pair if such meal isn't exist in the CRUD Memory
                // Удалят сохраненную пару дата/время и ID, если такая еда отсутсвует в CRUD памяти
                dateTimeWithIdMap.remove(updatedMeal.getDateTime());
                LOG.debug("did not updated a non-existent meal");
            }
        }
        LOG.debug("didn't updated a meal with a similar date/time of a meal in the CRUD memory");
    }

    @Override
    public Meal getMeal(int mealId) {
        LOG.debug("returns a meal on request from doGet");
        return mealMap.get(mealId);
    }

    @Override
    public void deleteMeal(Meal deletedMeal) {
        // Checks if the meal being deleted from CRUD Memory matches the requested one
        // Проверяет, соответствует удаляемая еда из CRUD памяти запрошенной к удалению
        if (mealMap.remove(deletedMeal.getId(), deletedMeal)) {
            // Deletes the date/time and ID pair of the deleted meal
            // if the ID being deleted meal matches the requested one
            // Удаляет пару дата/время и ID удаляемой еды, если ID
            // удаляемой еды соответствует запрошенной к удалению
            dateTimeWithIdMap.remove(deletedMeal.getDateTime(), deletedMeal.getId());
            LOG.debug("deleted a meal from the CRUD memory");
        } else {
            LOG.debug("didn't find such meal for delete from the CRUD memory");
        }
    }

    @Override
    public List<Meal> getAllMeals() {
        LOG.debug("returns a list of meals from the CRUD memory");
        return new ArrayList<>(mealMap.values());
    }
}
