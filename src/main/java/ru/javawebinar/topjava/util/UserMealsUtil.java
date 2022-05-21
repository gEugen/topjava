package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    // Return filtered list with excess. Implemented by cycles
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealWithExcessResult = new ArrayList<>();
        Map<LocalDate, Integer> mealsCaloriesPerDate = getMealsCaloriesPerDate(meals);

        for (UserMeal userMeal : meals) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            Integer mealCaloriesPerDate = mealsCaloriesPerDate.get(mealDate);
            LocalTime mealTime = userMeal.getDateTime().toLocalTime();

            if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), isExcess(mealCaloriesPerDate, caloriesPerDay));
                mealWithExcessResult.add(mealWithExcess);
            }
        }

        return mealWithExcessResult;
    }

    // Returns hashmap where a key is a meal date and a value is a total amount of meal calories per date
    private static Map<LocalDate, Integer> getMealsCaloriesPerDate(List<UserMeal> meals) {
        Map<LocalDate, Integer> mealsCaloriesPerDate = new HashMap<>();

        for (UserMeal userMeal : meals) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            mealsCaloriesPerDate.merge(mealDate, userMeal.getCalories(), Integer::sum);
        }

        return mealsCaloriesPerDate;
    }

    // Returns filtered list with excess. Implemented by streams
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // Get hashmap where a key is a meal date and a value is a total amount of meal calories per date
        Map<LocalDate, Integer> mealsCaloriesPerDate = meals
                .stream()
                .collect(Collectors.toMap(p -> p.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum, HashMap::new));

        // Get filtered list with excess

        return meals
                .stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess(mealsCaloriesPerDate.get(meal.getDateTime().toLocalDate()), caloriesPerDay)))
                .collect(Collectors.toList());
    }

    private static boolean isExcess(Integer mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate > caloriesPerDay;
    }
}
