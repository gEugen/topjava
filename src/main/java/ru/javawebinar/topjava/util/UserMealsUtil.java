package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static ru.javawebinar.topjava.util.SuperNovaUserMealWithExcessCollector.toSuperNovaUserMealWithExcessCollector;
import static ru.javawebinar.topjava.util.UserMealMultiCycleExtractor.extractUserMealWithExcessByCycles;
import static ru.javawebinar.topjava.util.UserMealOneCycleExtractor.extractUserMealWithExcessByOneCycle;
import static ru.javawebinar.topjava.util.UserMealStreamApiExtractor.extractUserMealWithExcessByStreams;

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

    // Returns filtered list with excess. Implemented by cycles
    // If method arguments are not valid returns an empty ArrayList.
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (isValidArguments(meals, startTime, endTime, caloriesPerDay)) {
            return new ArrayList<>();
        }
        return extractUserMealWithExcessByCycles(meals, startTime, endTime, caloriesPerDay);
    }

    // Returns filtered list with excess. Implemented by streams.
    // Method for base learning task.
    // If method arguments are not valid returns an empty ArrayList.
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (isValidArguments(meals, startTime, endTime, caloriesPerDay)) {
            return new ArrayList<>();
        }
        return extractUserMealWithExcessByStreams(meals, startTime, endTime, caloriesPerDay);
    }

    // Returns filtered list with excess. Implemented by custom collector.
    // Method for optional learning task.
    // First the method used a collector of UserMealWithExcessCollector class and its methods.
    // Second the method used a collector of NewUserMealWithExcessCollector class and its methods.
    // The SuperNovaUserMealWithExcessCollector and its methods used now.
    // The class of old collectors were left in the HW0 project for future comparative analysis.
    // If method arguments are not valid returns an empty ArrayList.
    public static List<UserMealWithExcess> filteredByStreamForOpt2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (isValidArguments(meals, startTime, endTime, caloriesPerDay)) {
            return new ArrayList<>();
        }
        return meals
                .stream()
                .collect(toSuperNovaUserMealWithExcessCollector(startTime, endTime, caloriesPerDay));
    }

    // Returns filtered list with excess. Implemented by one cycle scan of List<UserMeal> meals
    // Method for optional learning task.
    // If method arguments are not valid returns an empty ArrayList.
    public static List<UserMealWithExcess> filteredByOneCycleForOpt2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (isValidArguments(meals, startTime, endTime, caloriesPerDay)) {
            return new ArrayList<>();
        }
        return extractUserMealWithExcessByOneCycle(meals, startTime, endTime, caloriesPerDay);
    }

    // Returns flag that method arguments are valid.
    private static boolean isValidArguments(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (meals != null) {
            return meals.size() == 0 || startTime == null || endTime == null || !startTime.isBefore(endTime) || caloriesPerDay <= 0;
        }

        return true;
    }
}
