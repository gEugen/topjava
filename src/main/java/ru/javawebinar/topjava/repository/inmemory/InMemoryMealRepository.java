package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
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

    private final Map<Integer, Meal> repository;

    private final AtomicInteger counter = new AtomicInteger();

    public InMemoryMealRepository() {
        this.repository = new ConcurrentHashMap<>();
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            log.info("add meal {} by user with id={}", meal, meal.getUserId());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // handle case: update, but not present in storage
        log.info("update meal with id={}", meal.getId());
        Meal updatedMeal = repository.computeIfPresent(
                meal.getId(), (id, oldMeal) -> {
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
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(id, (v, meal) -> {
            if (repository.get(id).getUserId().equals(userId)) {
                result.set(true);
                return null;
            }
            return meal;
        });
        return result.get();
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal with id={} by user with id={}", id, userId);
        try {
            Meal meal = repository.get(id);
            return meal.getUserId() == userId ? meal : null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public List<Meal> getList(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("get meals by user with id={} from repository via predicate filter", userId);
        return repository.values().stream()
                .filter(meal -> {
                    if (startDate == null && endDate == null) {
                        return meal.getUserId().equals(userId);
                    } else {
                        return DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate.atStartOfDay(),
                                endDate.atTime(LocalTime.MAX).plusNanos(1)) && meal.getUserId().equals(userId);
                    }
                })
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

