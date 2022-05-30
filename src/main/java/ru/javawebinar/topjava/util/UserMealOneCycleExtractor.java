package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.MealValidator.isValidMeal;

public class UserMealOneCycleExtractor {
    private final Map<LocalDate, Integer> mealsCaloriesPerDate = new HashMap<>();
    private final List<UserMealWithExcess> mealsWithExcesses = new ArrayList<>();
    private int index;
    private final LocalTime  startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;

    private UserMealOneCycleExtractor(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    public static List<UserMealWithExcess> extractUserMealWithExcessByOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new UserMealOneCycleExtractor(startTime, endTime, caloriesPerDay).userMealByDayExtractCalculateFilterByTime(meals);
    }

    // Returns filtered list with excess.
    private List<UserMealWithExcess> userMealByDayExtractCalculateFilterByTime(List<UserMeal> meals) {
        index = meals.size() - 1;
        if (index >= 0) {
            userMealExtractFromMeals(meals);
        }

        return mealsWithExcesses;
    }

    // Calculates daily calorie excess and filters out meals in the given time interval.
    private void userMealExtractFromMeals(List<UserMeal> meals) {
        UserMeal userMeal = meals.get(index);
        if (isValidMeal(userMeal)) {
            LocalDate date = userMeal.getDateTime().toLocalDate();
            int calories = userMeal.getCalories();
            // Integer::sum <=> (v1, v2) -> v1 + v2)
            mealsCaloriesPerDate.merge(date, calories, Integer::sum);
        }

        if (index > 0) {
            index--;
           userMealExtractFromMeals(meals);
        }

        if (isValidMeal(userMeal)) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(),startTime, endTime)) {
                mealsWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), isExcess(mealsCaloriesPerDate.get(userMeal.getDateTime().toLocalDate()), caloriesPerDay)));
            }
        }
    }

    // Indicates the presence of an excess.
    private boolean isExcess(Integer mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate > caloriesPerDay;
    }

}
