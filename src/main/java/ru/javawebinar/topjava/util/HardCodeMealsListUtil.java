package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class HardCodeMealsListUtil {
    private final static List<Meal> hardCodeMeals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    private final static int caloriesPerDay = 2000;
    private final static LocalTime lowerLimit = LocalTime.of(0, 0);
    private final static LocalTime upperLimit = LocalTime.of(23, 59);

    public static List<Meal> getHardCodeMeals() {
        return hardCodeMeals;
    }

    public static int getCaloriesSetPoint() {
        return caloriesPerDay;
    }

    public static List<MealTo> getHardCodeMealsTo() {
        return MealsUtil.filteredByStreams(getHardCodeMeals(), lowerLimit, upperLimit, getCaloriesSetPoint());

    }
}
