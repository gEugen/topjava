package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotMealValid;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotMealValidById;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id, int authUserId) {
        checkNotMealValidById(repository.delete(id, authUserId), id, authUserId);
    }

    public Meal get(int id, int authUserId) {
        return checkNotMealValidById(repository.get(id, authUserId), id, authUserId);
    }

    public Collection<Meal> getAll(int authUserId) {
        return repository.getAll(authUserId);
    }

    public void update(Meal meal) {
        checkNotMealValid(repository.save(meal), meal);
    }
}