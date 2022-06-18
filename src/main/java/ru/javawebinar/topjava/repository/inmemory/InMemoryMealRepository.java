package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository;
    private final AtomicInteger counter = new AtomicInteger();

    public InMemoryMealRepository() {
        this.repository = new ConcurrentHashMap<>();
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // handle case: update, but not present in storage
        return meal.getUserId() == repository.computeIfPresent(
                        meal.getId(), (id, oldMeal) ->
                                meal.getUserId() == repository.get(meal.getId()).getUserId() ? meal : oldMeal)
                .getUserId() ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(id, (v, meal) -> {
            if (repository.get(id).getUserId().equals(userId)) {
                result.set(true);
                return repository.remove(id);
            }
            return meal;
        });
        return result.get();
    }

    @Override
    public Meal get(int id, int userId) {
        AtomicReference<Meal> result = new AtomicReference<>();
        repository.computeIfPresent(id, (key, meal) -> {
            if (meal.getUserId().equals(userId)) {
                result.set(meal);
            }
            return meal;
        });
        return result.get();
    }

    @Override
    public Collection<Meal> getAll(Integer authUserId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId().equals(authUserId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

