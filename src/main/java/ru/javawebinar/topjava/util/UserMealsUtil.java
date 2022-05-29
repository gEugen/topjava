package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.NewUserMealWithExcessCollector.toNewUserMealWithExcessCollector;
import static ru.javawebinar.topjava.util.UserMealWithExcessCollector.toUserMealWithExcessCollector;

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

        List<UserMealWithExcess> mealsToByOpt2 = filteredByStreamForOpt2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByOpt2.forEach(System.out::println);

        System.out.println(filteredByOneCycleForOpt2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
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

    // Returns hashmap where a key is a meal date and a value is a total amount of meal calories per date.
    // Method for base learning task.
    private static Map<LocalDate, Integer> getMealsCaloriesPerDate(List<UserMeal> meals) {
        Map<LocalDate, Integer> mealsCaloriesPerDate = new HashMap<>();

        for (UserMeal userMeal : meals) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            // Integer::sum <=> (v1, v2) -> v1 + v2)
            mealsCaloriesPerDate.merge(mealDate, userMeal.getCalories(), Integer::sum);
        }

        return mealsCaloriesPerDate;
    }

    // Returns filtered list with excess. Implemented by streams.
    // Method for base learning task.
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // Get hashmap where a key is a meal date and a value is a total amount of meal calories per date
        Map<LocalDate, Integer> mealsCaloriesPerDate = meals
                .stream()
                // UserMeal::getCalories <=> v -> v.getCalories() and Integer::sum <=> (v1, v2) -> v1 + v2
                .collect(Collectors.toMap(p -> p.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum, HashMap::new));

        // Get filtered list with excess
        return meals
                .stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess(mealsCaloriesPerDate.get(meal.getDateTime().toLocalDate()), caloriesPerDay)))
                .collect(Collectors.toList());
    }

    // Returns filtered list with excess. Implemented by custom collector.
    // Method for optional learning task.
    // Previously, the method used a collector of UserMealWithExcessCollector class and its methods.
    // The NewUserMealWithExcessCollector and its methods used now.
    // The class of old collector was left in the HW0 project for future comparative analysis.
    public static List<UserMealWithExcess> filteredByStreamForOpt2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals
                .stream()
                .collect(toNewUserMealWithExcessCollector(startTime, endTime, caloriesPerDay));
    }

    // Returns filtered list with excess. Implemented by one cycle scan of List<UserMeal> meals
    // Method for optional learning task.
    public static List<UserMealWithExcess> filteredByOneCycleForOpt2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new UserMealExtractor().userMealByDayExtractCalculateFilterByTime(meals, caloriesPerDay, startTime, endTime);
    }

    // Indicates the presence of an excess.
    private static boolean isExcess(Integer mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate > caloriesPerDay;
    }
}
