package ru.javawebinar.topjava.servise;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalTime;
import java.util.List;

public interface CrudMemoryService {
    List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay);

    void saveMeal(Integer id, Meal meal);

    void deleteMeal(Integer id);

    Meal getMeal(Integer id);
}
