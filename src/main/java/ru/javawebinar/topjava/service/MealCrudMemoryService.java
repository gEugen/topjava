package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealCrudMemoryService {
    void addMeal(Meal formMeal);

    void updateMeal(Meal formMeal);

    Meal getMeal(int mealId);

    void deleteMeal(Meal deletedMeal);

    List<Meal> getAllMeals();
}
