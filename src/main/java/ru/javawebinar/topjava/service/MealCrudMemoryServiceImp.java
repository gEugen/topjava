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
}
