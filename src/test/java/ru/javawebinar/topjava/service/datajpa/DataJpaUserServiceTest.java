package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.service.UserService;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER_WITH_MEALS.assertMatch(user, userWithMeals);
    }

    @Test
    public void getWithNoMeals() {
        USER_MATCHER_WITH_MEALS.assertMatch(service.getWithMeals(GUEST_ID), guest);
    }
}
