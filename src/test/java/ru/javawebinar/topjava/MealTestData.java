package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL1_ID = START_SEQ + 3;
    public static final int MEAL2_ID = START_SEQ + 4;
    public static final int MEAL3_ID = START_SEQ + 5;
    public static final int MEAL4_ID = START_SEQ + 6;
    public static final int MEAL5_ID = START_SEQ + 7;
    public static final int MEAL6_ID = START_SEQ + 8;
    public static final int MEAL7_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 10;

    public static final Meal meal1 =
            new Meal(MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal2 =
            new Meal(MEAL2_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal3 =
            new Meal(MEAL3_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal4 =
            new Meal(MEAL4_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на границе", 100);
    public static final Meal meal5 =
            new Meal(MEAL5_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal6 =
            new Meal(MEAL6_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal meal7 =
            new Meal(MEAL7_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 19, 0), "Ужин2", 410);

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
        Meal updated = new Meal(meal7);
        updated.setDateTime(new java.sql.Timestamp(new Date().getTime()).toLocalDateTime());
        updated.setDescription("UpdatedDescription");
        updated.setCalories(330);
        return updated;
    }

    public static int getSomeoneElsesTestId() {
        return MEAL5_ID;
    }

    public static int getTestId() {
        return MEAL1_ID;
    }

    public static int getUpdatedId() {
        return MEAL7_ID;
    }

    public static int getNotFoundId() {
        return NOT_FOUND;
    }

    public static Meal getTestMeal() {
        return meal1;
    }

    public static LocalDate getStartDate() {
        return LocalDateTime.of(2020, Month.JANUARY, 29, 0, 0).toLocalDate();
    }

    public static LocalDate getEndDate() {
        return LocalDateTime.of(2020, Month.JANUARY, 30, 0, 0).toLocalDate();
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
