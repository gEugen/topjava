package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealCrudMemory;

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
        mealMemory.add(newMeal);
    }

    @Override
    public void update(Meal updatedMeal) {
        int testId = dateTimeWithIdMap.merge(updatedMeal.getDateTime(), updatedMeal.getId(), ((v1, v2) -> v1));
        if (testId == updatedMeal.getId()) {
            if (mealMap.replace(updatedMeal.getId(), updatedMeal) != null) {
                LOG.debug("updated a meal in the CRUD memory");

            } else {
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
    public void deleteMeal(Integer mealId, Meal deletedMeal) {
        // Checks if the meal being deleted matches the requested one
        if (mealMap.remove(mealId, deletedMeal)) {
            // Deletes the date/time of the deleted meal if the ID being deleted matches the requested one
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
