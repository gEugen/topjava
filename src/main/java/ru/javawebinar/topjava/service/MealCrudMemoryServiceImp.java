package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealCrudMemory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MealCrudMemoryServiceImp implements MealCrudMemoryService {
    private static class CrudMemoryServiceHolder {
        public static final MealCrudMemoryService HOLDER_INSTANCE = new MealCrudMemoryServiceImp();
    }

    public static MealCrudMemoryService getInstance() {
        return MealCrudMemoryServiceImp.CrudMemoryServiceHolder.HOLDER_INSTANCE;
    }

    public synchronized List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return MealCrudMemory.getAll(startTime, endTime, caloriesPerDay);
    }

    public synchronized void saveMeal(Meal mealAccumulator, LocalDateTime dateTime, String description, int calories) {
        MealCrudMemory.add(mealAccumulator, dateTime, description, calories);
    }

    public synchronized void deleteMeal(LocalDateTime dateTime) {
        MealCrudMemory.delete(dateTime);
    }

    public synchronized Meal getMeal(LocalDateTime dateTime) {
        return MealCrudMemory.get(dateTime);
    }

    @Override
    public synchronized Meal getDefaultMeal() {
        return MealCrudMemory.getDefault();
    }
}
