package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app-general.xml",
        "classpath:spring/spring-app-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest extends TestCase {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), getUserId());
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, getUserId()), newMeal);
    }

    @Test
    public void duplicateDateCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Duplicate", 500), getUserId()));
    }

    @Test
    public void delete() {
        service.delete(getTestId(), getUserId());
        assertThrows(NotFoundException.class, () -> service.get(getTestId(), getUserId()));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(getNotFoundId(), getUserId()));
    }

    @Test
    public void get() {
        Meal meal = service.get(getTestId(), getUserId());
        assertMatch(meal, getTestMeal());
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(getNotFoundId(), getUserId()));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, getAdminId());
        assertMatch(service.get(getUpdatedId(), getAdminId()), updated);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(getUserId());
        assertMatch(all, meal05, meal04, meal03);
    }

    @Test
    public void getSomeoneElses() {
        assertThrows(NotFoundException.class, () -> service.get(getSomeoneElsesTestId(), getUserId()));
    }

    @Test
    public void deleteSomeoneElses() {
        assertThrows(NotFoundException.class, () -> service.delete(getSomeoneElsesTestId(), getUserId()));
    }

    @Test
    public void updateSomeoneElses() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), getUserId()));
    }
}