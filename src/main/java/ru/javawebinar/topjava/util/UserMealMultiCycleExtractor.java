package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMealMultiCycleExtractor {
    private final Map<LocalDate, Integer> mealsCaloriesPerDate = new HashMap<>();
    private final List<UserMealWithExcess> mealsWithExcesses = new ArrayList<>();
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;

    private UserMealMultiCycleExtractor(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    public static List<UserMealWithExcess> extractUserMealWithExcessByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new UserMealMultiCycleExtractor(startTime, endTime, caloriesPerDay).userMealByDayExtractCalculateFilterByTime(meals);
    }

    // Return filtered list with excess.
    private List<UserMealWithExcess> userMealByDayExtractCalculateFilterByTime(List<UserMeal> meals) {
        Map<LocalDate, Integer> mealsCaloriesPerDate = getMealsCaloriesPerDate(meals);

        for (UserMeal userMeal : meals) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            Integer mealCaloriesPerDate = mealsCaloriesPerDate.get(mealDate);
            LocalTime mealTime = userMeal.getDateTime().toLocalTime();

            if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), isExcess(mealCaloriesPerDate, caloriesPerDay));
                mealsWithExcesses.add(mealWithExcess);
            }
        }

        return mealsWithExcesses;
    }

    // Returns hashmap where a key is a meal date and a value is a total amount of meal calories per date.
    // Method for base learning task.
    private Map<LocalDate, Integer> getMealsCaloriesPerDate(List<UserMeal> meals) {
        for (UserMeal userMeal : meals) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            // Integer::sum <=> (v1, v2) -> v1 + v2)
            mealsCaloriesPerDate.merge(mealDate, userMeal.getCalories(), Integer::sum);
        }

        return mealsCaloriesPerDate;
    }

    // Indicates the presence of an excess.
    private boolean isExcess(Integer mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate > caloriesPerDay;
    }
}
