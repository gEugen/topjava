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

import static ru.javawebinar.topjava.util.MealValidator.isValidMeal;

// The class of old collector was left in the HW0 project for future comparative analysis.
public class NewUserMealWithExcessCollector implements Collector<UserMeal, Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>>{
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;

    public NewUserMealWithExcessCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    // Returns a new custom collector.
    public static NewUserMealWithExcessCollector toNewUserMealWithExcessCollector (LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new NewUserMealWithExcessCollector(startTime, endTime,caloriesPerDay);
    }

    // Returns a function that creates an empty accumulator as a map
    // where the key is the date, the value is the list of meals for that date.
    @Override
    public Supplier<Map<LocalDate, List<UserMeal>>> supplier() {
        return LinkedHashMap::new;
    }

    // Accepts two arguments: mutable result container (accumulator) and the stream element.
    // Returns a function which performs the reduction operation.
    @Override
    public BiConsumer<Map<LocalDate, List<UserMeal>>, UserMeal> accumulator() {
        return (map, val) -> {
            if (isValidMeal(val)) {
                LocalDate date = val.getDateTime().toLocalDate();

                try {
                    map.get(date).add(val);
                } catch (NullPointerException e) {
                    List<UserMeal> list = new ArrayList<>();
                    list.add(val);
                    map.put(date, list);
                }
            }
        };
    }

    // The combiner() method is used when the stream is collected in parallel to return a function
    // which knows how to merge two accumulators. Returns one accumulator now.
    @Override
    public BinaryOperator<Map<LocalDate, List<UserMeal>>> combiner() {
        return (l, r) -> { l.putAll(r); return l; };
    }

    //Returns a function which performs the final transformation from the intermediate
    // result container to the final result.
    // The AtomicInteger is used because variable used in lambda expression should be final or effectively final.
    @Override
    public Function<Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>> finisher() {
        return map -> map.values().stream()
                .flatMap(subList -> {
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

    // Indicates the presence of an excess.
    private boolean isExcess(AtomicInteger mealCaloriesPerDate, int caloriesPerDay) {
        return mealCaloriesPerDate.get() > caloriesPerDay;
    }
}
