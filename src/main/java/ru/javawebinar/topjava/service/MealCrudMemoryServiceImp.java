package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealCrudMemory;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        LOG.debug("returns a list of meals from CRUD memory");
        return MealsUtil.filteredByStreams(new ArrayList<>(mealMap.values()), startTime, endTime, caloriesPerDay);
    }

    @Override
    public void add(Meal newMeal) {
        LOG.debug("adds a meal to CRUD memory");
        mealMemory.add(newMeal);
    }

    @Override
    public void update(Meal updatedMeal) {
        if (mealMap.replace(updatedMeal.getId(), updatedMeal) != null) {
            LOG.debug("added a meal to CRUD memory");

        } else {
            LOG.debug("did not add a meal with a similar date and time");
        }
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
            LOG.debug("deleted a meal from CRUD memory");
        } else {
            LOG.debug("didn't find such meal for delete from CRUD memory");
        }
    }
}
