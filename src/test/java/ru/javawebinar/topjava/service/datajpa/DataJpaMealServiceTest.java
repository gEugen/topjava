package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER_WITH_USER.assertMatch(actual, adminMeal1WithUser);
    }

    @Test
    public void getWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(NOT_FOUND, USER_ID));
    }

    @Test
    public void getWithUserNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, ADMIN_ID));
    }
}
