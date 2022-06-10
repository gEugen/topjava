package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalTime;
import java.util.List;

public interface MealCrudMemoryService {
    List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay);

    void add(Meal formMeal);

    void update(Meal formMeal);

    Meal getMeal(int mealId);

    void deleteMeal(Integer id, Meal deletedMeal);
}
