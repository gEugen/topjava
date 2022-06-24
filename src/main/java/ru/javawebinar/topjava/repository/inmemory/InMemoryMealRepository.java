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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, UserMeals> repository;

    private final AtomicInteger counter = new AtomicInteger();

    public InMemoryMealRepository() {
        this.repository = new ConcurrentHashMap<>();
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        UserMeals userMeals = repository.computeIfAbsent(userId, k -> {
            log.info("create meal repository for user with id={}", userId);
            return new UserMeals();
        });
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            log.info("add meal {} by user with id={}", meal, meal.getUserId());
            userMeals.map.put(meal.getId(), meal);
            return meal;
        }

        Meal updatedMeal = userMeals.map.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (oldMeal.getUserId().equals(userId)) {
                meal.setUserId(userId);
                return meal;
            } else {
                return oldMeal;
            }
        });
        if (updatedMeal != null) {
            log.info("update meal with id={} by user with id={}", meal.getId(), meal.getUserId());
            return updatedMeal.getUserId().equals(meal.getUserId()) ? meal : null;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal with id={} by user with id={}", id, userId);
        UserMeals userMeals = repository.computeIfPresent(userId, (k, meals) -> meals);
        AtomicBoolean result = new AtomicBoolean(false);
        if (userMeals != null) {
            userMeals.map.computeIfPresent(id, (k, meal) -> {
                if (meal.getUserId().equals(userId)) {
                    result.set(true);
                    return null;
                } else {
                    return meal;
                }
            });
        }
        return result.get();
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal with id={} by user with id={}", id, userId);
        UserMeals userMeals = repository.computeIfPresent(userId, (k, meals) -> meals);
        if (userMeals != null) {
            Meal takenOutMeal = userMeals.map.computeIfPresent(id, (k, meal) -> meal);
            if (takenOutMeal != null) {
                if (takenOutMeal.getUserId().equals(userId)) {
                    return takenOutMeal;
                }
            }
        }
        return null;
    }

    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("get meals by user with id={} from repository", userId);
        UserMeals userMeals = repository.computeIfPresent(userId, (k, meals) -> meals);
        if (userMeals != null) {
            return userMeals.map.values().stream()
                    .filter(meal -> {
                        if (startDate == LocalDate.MIN && endDate == LocalDate.MAX) {
                            return meal.getUserId().equals(userId);
                        } else {
                            LocalDateTime endLdt;
                            if (!endDate.equals(LocalDate.MAX)) {
                                endLdt = endDate.atTime(LocalTime.MAX).plusNanos(1);
                            } else {
                                endLdt = endDate.atTime(LocalTime.MAX);
                            }
                            return DateTimeUtil.isBetweenHalfOpen(
                                    meal.getDateTime(), startDate.atStartOfDay(), endLdt) && meal.getUserId().equals(userId);
                        }
                    })
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private class UserMeals {
        private final Map<Integer, Meal> map;

        private UserMeals() {
            this.map = new ConcurrentHashMap<>();
        }
    }
}

