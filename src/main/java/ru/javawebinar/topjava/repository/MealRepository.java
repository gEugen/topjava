package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {
    // null if updated meal does not belong to userId
    Meal save(Meal meal);

    // false if meal does not belong to userId
    boolean delete(int id, int authUserId);

    // null if meal does not belong to userId
    Meal get(int id, int authUserId);

    // ORDERED dateTime desc
    List<Meal> getAll(int authUserId, LocalDate startDate, LocalDate endDate);
}
