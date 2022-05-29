package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealWithExcessCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>>{
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;

    // Designed to store a table of calories received by day.
    // Keys are days as LocalDate and values are calories as Integer.
    private final Map<LocalDate, Integer> mealsCaloriesPerDate = new HashMap<>();

    public UserMealWithExcessCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    // Returns a new custom collector.
    public static UserMealWithExcessCollector toUserMealWithExcessCollector (LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new UserMealWithExcessCollector(startTime, endTime,caloriesPerDay);
    }

    // Returns a function that creates an empty accumulator.
    @Override
    public Supplier<List<UserMeal>> supplier() {
        return ArrayList::new;
    }

    // Accepts two arguments: mutable result container (accumulator) and the stream element.
    // Returns a function which performs the reduction operation.
    @Override
    public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
        return (list, val) -> {
            mealsCaloriesPerDate.merge(val.getDateTime().toLocalDate(), val.getCalories(), Integer::sum);
            list.add(val);
        };
    }

    // The combiner() method is used when the stream is collected in parallel to return a function
    // which knows how to merge two accumulators. Returns one accumulator now.
    @Override
    public BinaryOperator<List<UserMeal>> combiner() {
        return (l, r) -> { l.addAll(r); return l; };
    }

    //Returns a function which performs the final transformation from the intermediate
    // result container to the final result.
    @Override
    public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
        return list -> list.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess(mealsCaloriesPerDate.get(meal.getDateTime().toLocalDate()), caloriesPerDay)))
                .collect(Collectors.toList());
    }

    // Returns an immutable set of Characteristics which define the behavior of the collector.
    // This is used to check which kind of optimizations can be done during the reduction process.
    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }

    // Indicates the presence of an excess.
    private boolean isExcess(Integer mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate > caloriesPerDay;
    }
}
