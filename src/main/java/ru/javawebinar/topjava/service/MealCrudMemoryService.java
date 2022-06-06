package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealCrudMemoryService {
    List<MealTo> getMeals(LocalTime startTime, LocalTime endTime, int caloriesPerDay);
}
