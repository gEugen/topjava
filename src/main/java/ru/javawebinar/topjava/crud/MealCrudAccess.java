package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealCrudAccess {
    void add(Meal meal);

    void update(Meal meal);

    Meal get(int id);

    void delete(Meal meal);

    List<Meal> getAll();
}
