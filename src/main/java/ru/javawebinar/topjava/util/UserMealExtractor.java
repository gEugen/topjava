package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserMealExtractor {
    private final Map<LocalDate, Integer> mealsCaloriesPerDate = new HashMap<>();
    private final List<UserMealWithExcess> mealsWithExcesses = new ArrayList<>();
    private int index;

    // Returns filtered list with excess.
    public List<UserMealWithExcess> userMealByDayExtractCalculateFilterByTime(List<UserMeal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        index = meals.size() - 1;
        if (index >= 0) {
            userMealExtractFromMeals(meals, caloriesPerDay, startTime, endTime);
        }

        return mealsWithExcesses;
    }

    // Calculates daily calorie excess and filters out meals in the given time interval.
    private void userMealExtractFromMeals(List<UserMeal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        UserMeal userMeal = meals.get(index);
        LocalDate date = userMeal.getDateTime().toLocalDate();
        int calories = userMeal.getCalories();
        // Integer::sum <=> (v1, v2) -> v1 + v2)
        mealsCaloriesPerDate.merge(date, calories, Integer::sum);

        if (index > 0) {
            index--;
           userMealExtractFromMeals(meals, caloriesPerDay, startTime, endTime);
        }

        if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(),startTime, endTime)) {
            mealsWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), isExcess(mealsCaloriesPerDate.get(date), caloriesPerDay)));
        }
    }

    // Indicates the presence of an excess.
    private boolean isExcess(Integer mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate > caloriesPerDay;
    }
}
