package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SuperNovaUserMealWithExcessCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>>{
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;

    public SuperNovaUserMealWithExcessCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    // Returns a function that creates an empty accumulator as a list of meals.
    @Override
    public Supplier<List<UserMeal>> supplier() {
        return ArrayList::new;
    }

    // Accepts two arguments: mutable result container (accumulator) and the stream element.
    // Returns a function which performs the reduction operation.
    @Override
    public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
        return List::add;
    }

    // The combiner() method is used when the stream is collected in parallel to return a function
    // which knows how to merge two accumulators. Returns one accumulator now.
    @Override
    public BinaryOperator<List<UserMeal>> combiner() {
        return (l, r) -> { l.addAll(r); return l; };
    }

    // Returns a function which performs the final transformation from the intermediate
    // result container via Map<LocalDate, List<UserMeal>> to the final result.
    @Override
    public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
        return meals -> meals.stream()
                    .collect(Collectors.groupingBy((UserMeal userMeal) -> userMeal.getDateTime().toLocalDate()))
                    .values().stream()
                    .flatMap(subList -> {
                        // The AtomicInteger is used because variable used in lambda expression should be final or effectively final.
                        AtomicInteger temp = new AtomicInteger();
                        // UserMeal::getCalories <=> meal -> meal.getCalories()
                        temp.set(subList.stream().mapToInt(UserMeal::getCalories).sum());

                        return subList.stream()
                                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess(temp, caloriesPerDay)));
                    })
                    .collect(Collectors.toList());
    }

    // Returns an immutable set of Characteristics which define the behavior of the collector.
    // This is used to check which kind of optimizations can be done during the reduction process.
    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }

    // Returns a new custom collector.
    public static SuperNovaUserMealWithExcessCollector toSuperNovaUserMealWithExcessCollector (LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new SuperNovaUserMealWithExcessCollector(startTime, endTime,caloriesPerDay);
    }

    // Indicates the presence of an excess.
    private boolean isExcess(AtomicInteger mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate.get() > caloriesPerDay;
    }
}
