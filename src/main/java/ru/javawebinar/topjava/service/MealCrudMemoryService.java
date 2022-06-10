package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface MealCrudMemoryService {
    void add(Meal formMeal);

    void update(Meal formMeal);

    Meal getMeal(int mealId);

    void deleteMeal(Integer id, Meal deletedMeal);

    List<Meal> getAllMeals();
}
