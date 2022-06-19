package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        checkNotValidAuthUserId(meal);
        return repository.save(meal);
    }

    public void delete(int id, int authUserId) {
        checkNotValidResultById(repository.delete(id, authUserId), id, authUserId);
    }

    public Meal get(int id, int authUserId) {
        return checkNotValidResultById(repository.get(id, authUserId), id, authUserId);
    }

    public List<Meal> getAll(int authUserId, LocalDate startDate, LocalDate endDate) {
        return repository.getAll(authUserId, startDate, endDate);
    }

    public void update(Meal meal) {
        checkNotValidAuthUserId(meal);
        checkNotValidResult(repository.save(meal), meal);
    }
}