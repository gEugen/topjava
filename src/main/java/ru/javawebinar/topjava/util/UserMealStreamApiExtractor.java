package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMealStreamApiExtractor {
    private Map<LocalDate, Integer> mealsCaloriesPerDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;

    private UserMealStreamApiExtractor(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    public static List<UserMealWithExcess> extractUserMealWithExcessByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new UserMealStreamApiExtractor(startTime, endTime, caloriesPerDay).userMealByDayExtractCalculateFilterByTime(meals);
    }

    private List<UserMealWithExcess> userMealByDayExtractCalculateFilterByTime(List<UserMeal> meals) {
        // Get hashmap where a key is a meal date and a value is a total amount of meal calories per date
        mealsCaloriesPerDate = meals.stream()
                // UserMeal::getCalories <=> v -> v.getCalories() and Integer::sum <=> (v1, v2) -> v1 + v2
                .collect(Collectors.toMap(p -> p.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum, HashMap::new));

        // Get filtered list with excess
        return meals
                .stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess(mealsCaloriesPerDate.get(meal.getDateTime().toLocalDate()), caloriesPerDay)))
                .collect(Collectors.toList());
    }

    // Indicates the presence of an excess.
    private static boolean isExcess(Integer mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate > caloriesPerDay;
    }
}
