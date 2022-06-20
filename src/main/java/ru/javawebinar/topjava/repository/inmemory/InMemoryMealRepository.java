package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

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
    public Meal save(Meal meal, int authUserId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(authUserId);
            log.info("add meal {} by user with id={}", meal, meal.getUserId());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // handle case: update, but not present in storage
        log.info("update meal with id={}", meal.getId());
        Meal updatedMeal = repository.computeIfPresent(
                meal.getId(), (id, oldMeal) -> {
                    if (oldMeal.getUserId().equals(authUserId)) {
                        meal.setUserId(authUserId);
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
    public boolean delete(int id, int authUserId) {
        log.info("delete meal with id={} by user with id={}", id, authUserId);
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(id, (v, meal) -> {
            if (repository.get(id).getUserId().equals(authUserId)) {
                result.set(true);
                return repository.remove(id);
            }
            return meal;
        });
        return result.get();
    }

    @Override
    public Meal get(int id, int authUserId) {
        log.info("get meal with id={} by user with id={}", id, authUserId);
        AtomicReference<Meal> result = new AtomicReference<>();
        repository.computeIfPresent(id, (key, meal) -> {
            if (meal.getUserId().equals(authUserId)) {
                result.set(meal);
            }
            return meal;
        });
        return result.get();
    }

    @Override
    public List<Meal> getAll(int authUserId, LocalDate startDate, LocalDate endDate) {
        log.info("get meals by user with id={}", authUserId);
        LocalDateTime startLdt = startDate.atStartOfDay();
        LocalDateTime endLdt;
        if (!endDate.isEqual(LocalDate.MAX)) {
            endLdt = endDate.plusDays(1).atStartOfDay();
        } else {
            endLdt = endDate.atTime(LocalTime.MAX);
        }
        return repository.values().stream()
                .filter(meal ->
                        (meal.getUserId().equals(authUserId) && isBetweenHalfOpen(meal.getDateTime(), startLdt,
                                endLdt)))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

