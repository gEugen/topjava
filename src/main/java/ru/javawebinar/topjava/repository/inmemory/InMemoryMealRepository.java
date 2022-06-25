package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository;

    private final AtomicInteger counter = new AtomicInteger();

    public InMemoryMealRepository() {
        this.repository = new ConcurrentHashMap<>();
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, k -> {
            log.info("create meal repository for user with id={}", userId);
            return new ConcurrentHashMap<>();
        });

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            log.info("add meal {} by user with id={}", meal, userId);
            userMeals.put(meal.getId(), meal);
            return meal;
        }

        Meal updatedMeal = userMeals.computeIfPresent(meal.getId(), (k, v) -> meal);
        if (updatedMeal != null) {
            log.info("update meal with id={} by user with id={}", meal.getId(), userId);
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        AtomicBoolean result = new AtomicBoolean(false);
        if (userMeals != null) {
            userMeals.computeIfPresent(id, (k, meal) -> {
                log.info("delete meal with id={} by user with id={}", id, userId);
                result.set(true);
                return null;
            });
        }
        return result.get();
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            Meal takenOutMeal = userMeals.get(id);
            if (takenOutMeal != null) {
                log.info("get meal with id={} by user with id={}", id, userId);
                return takenOutMeal;
            }
        }
        return null;
    }

    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("get meals by user with id={} from repository", userId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return userMeals.values().stream()
                    .filter(meal -> {
                        LocalDateTime endLdt = !endDate.equals(LocalDate.MAX) ?
                                endDate.atTime(LocalTime.MAX).plusNanos(1) : endDate.atTime(LocalTime.MAX);

                        return DateTimeUtil.isBetweenHalfOpen(
                                meal.getDateTime(), startDate.atStartOfDay(), endLdt);
                    })
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("get all meals by user with id={} from repository", userId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return userMeals.values().stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

