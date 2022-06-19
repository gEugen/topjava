package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll(int authUserId) {
        log.info("getAll");
        return getTos(service.getAll(authUserId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id, int authUserId) {
        log.info("get {}", id);
        return service.get(id, authUserId);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id, int authUserId) {
        log.info("delete {}", id);
        service.delete(id, authUserId);
    }

    public void update(Meal meal, int authUserId) {
        log.info("update {}", meal);
        service.update(meal);
    }
}
