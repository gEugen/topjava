package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

@Controller
public class MealRestController extends AbstractMealController {

    @Override
    public List<MealTo> getAll(int authUserId) {
        return super.getAll(authUserId);
    }

    @Override
    public Meal get(int id, int authUserId) {
        return super.get(id, authUserId);
    }

    @Override
    public Meal create(Meal meal) {
        return super.create(meal);
    }

    @Override
    public void delete(int id, int authUserId) {
        super.delete(id, authUserId);
    }

    @Override
    public void update(Meal meal, int authUserId) {
        super.update(meal, authUserId);
    }
}