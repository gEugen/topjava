package ru.javawebinar.topjava.servise;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.optional.CrudMemory.getStorage;

public class CrudMemoryServiceImp implements CrudMemoryService{
    public synchronized List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return MealsUtil.filteredByStreams(new ArrayList<>(getStorage().values()), startTime, endTime, caloriesPerDay);
    }

    public synchronized void saveMeal(Integer id, Meal meal) {
        getStorage().put(id, meal);
    }

    public synchronized void deleteMeal(Integer id) {
        getStorage().remove(id);
    }

    public synchronized Meal getMeal(Integer id) {
        return getStorage().get(id);
    }
}
