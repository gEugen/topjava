package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL_03_ID = START_SEQ + 3;
    public static final int MEAL_04_ID = START_SEQ + 4;
    public static final int MEAL_05_ID = START_SEQ + 5;
    public static final int MEAL_07_ID = START_SEQ + 7;
    public static final int MEAL_09_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 10;

    public static final Meal meal03 =
            new Meal(MEAL_03_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal04 =
            new Meal(MEAL_04_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal05 =
            new Meal(MEAL_05_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal09 =
            new Meal(MEAL_09_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(new java.sql.Timestamp(new Date().getTime()).toLocalDateTime(), "Перекус", 350);
    }

    public static int getUserId() {
        return USER_ID;
    }

    public static int getAdminId() {
        return ADMIN_ID;
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal09);
        updated.setDateTime(new java.sql.Timestamp(new Date().getTime()).toLocalDateTime());
        updated.setDescription("UpdatedDescription");
        updated.setCalories(330);
        return updated;
    }

    public static int getSomeoneElsesTestId() {
        return MEAL_07_ID;
    }

    public static int getTestId() {
        return MEAL_03_ID;
    }

    public static int getUpdatedId() {
        return MEAL_09_ID;
    }

    public static int getNotFoundId() {
        return NOT_FOUND;
    }

    public static Meal getTestMeal() {
        return meal03;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime").isEqualTo(expected);
    }
}
