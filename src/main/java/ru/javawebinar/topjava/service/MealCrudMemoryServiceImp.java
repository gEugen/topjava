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

    public List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return MealCrudMemory.getAll(startTime, endTime, caloriesPerDay);
    }

    public void saveMeal(Meal mealAccumulator, LocalDateTime dateTime, String description, int calories) {
        MealCrudMemory.addOrUpdate(mealAccumulator, dateTime, description, calories);
    }

    public void deleteMeal(LocalDateTime dateTime) {
        MealCrudMemory.delete(dateTime);
    }

    public Meal getMeal(LocalDateTime dateTime) {
        return MealCrudMemory.get(dateTime);
    }

    @Override
    public Meal getDefaultMeal() {
        return MealCrudMemory.getDefault();
    }
}
