package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, Integer userId) {
        checkNotValidUserId(meal, userId);
        return repository.save(meal, userId);
    }

    public void update(Meal meal, Integer userId) {
        checkNotValidUserId(meal, userId);
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getFilteredByDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getSomeViaPredicateFilter(userId,
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate.atStartOfDay(),
                        endDate.atTime(LocalTime.MAX)));
    }

    public List<Meal> getAll(int userId) {
        return repository.getSomeViaPredicateFilter(userId, meal -> meal.getUserId().equals(userId));
    }
}