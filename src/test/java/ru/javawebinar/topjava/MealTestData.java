package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL1_ID = START_SEQ + 3;
    public static final int USER_MEAL2_ID = START_SEQ + 4;
    public static final int USER_MEAL3_ID = START_SEQ + 5;
    public static final int ADMIN_MEAL4_ID = START_SEQ + 6;
    public static final int ADMIN_MEAL5_ID = START_SEQ + 7;
    public static final int ADMIN_MEAL6_ID = START_SEQ + 8;
    public static final int ADMIN_MEAL7_ID = START_SEQ + 9;
    public static final int ADMIN_MEAL8_ID = START_SEQ + 10;
    public static final int ADMIN_MEAL9_ID = START_SEQ + 11;
    public static final int ADMIN_MEAL10_ID = START_SEQ + 12;
    public static final int NOT_FOUND = 100;
    public static final LocalDate START_DATE = LocalDate.of(2020, Month.JANUARY, 29);
    public static final LocalDate END_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDateTime DATE_TIME = LocalDateTime.of(2022, Month.JUNE, 30, 22, 41);

    public static final Meal userMeal1 =
            new Meal(USER_MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal2 =
            new Meal(USER_MEAL2_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal3 =
            new Meal(USER_MEAL3_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal adminMeal4 =
            new Meal(ADMIN_MEAL4_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на границе", 100);
    public static final Meal adminMeal5 =
            new Meal(ADMIN_MEAL5_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 1000);
    public static final Meal adminMeal6 =
            new Meal(ADMIN_MEAL6_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal adminMeal7 =
            new Meal(ADMIN_MEAL7_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 19, 0), "Ужин2", 410);
    public static final Meal adminMeal8 =
            new Meal(ADMIN_MEAL8_ID, LocalDateTime.of(2020, Month.JANUARY, 27, 7, 0), "Завтрак на траве", 210);
    public static final Meal adminMeal9 =
            new Meal(ADMIN_MEAL9_ID, LocalDateTime.of(2020, Month.JANUARY, 28, 15, 0), "Полдник", 210);
    public static final Meal adminMeal10 =
            new Meal(ADMIN_MEAL10_ID, LocalDateTime.of(2020, Month.JANUARY, 29, 0, 0), "Поздний ланч", 210);

    public static Meal getNew() {
        return new Meal(DATE_TIME, "Перекус", 350);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(adminMeal7);
        updated.setDateTime(DATE_TIME);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
