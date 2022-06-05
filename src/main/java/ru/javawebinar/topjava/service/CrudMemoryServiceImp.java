package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.optional.CrudMemory;
import ru.javawebinar.topjava.optional.IdGenerator;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class CrudMemoryServiceImp implements CrudMemoryService{
    public synchronized List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return CrudMemory.getAll(startTime, endTime, caloriesPerDay);
    }

    public synchronized void saveMeal(LocalDateTime dateTime, String description, int calories) {
        CrudMemory.add(dateTime, description, calories);
    }

    public synchronized void deleteMeal(LocalDateTime dateTime) {
        CrudMemory.delete(dateTime);
    }

    public synchronized Meal getMeal(LocalDateTime dateTime) {
        return CrudMemory.get(dateTime);
    }

    @Override
    public synchronized Meal getDefaultMeal() {
        return CrudMemory.getDefault();
    }
}
