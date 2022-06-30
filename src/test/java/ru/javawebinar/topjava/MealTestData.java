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
    public static final int MEAL_07_ID = START_SEQ + 7;
    public static final int MEAL_09_ID = START_SEQ + 9;

    public static final Meal meal09 =
            new Meal(MEAL_09_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static int getUserId() {
        return USER_ID;
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
}
