package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;

// Provides returning a flag that the UserMeal type object has valid content.
public class MealValidator {
    private boolean validate(UserMeal meal) {
        boolean valid = meal != null;
        if (valid) valid = meal.getDateTime() != null;
        if (valid) valid = meal.getDescription() != null;
        if (valid) valid = meal.getCalories() > 0;

        return valid;
    }

    public static boolean isValidMeal(UserMeal meal) {
        return new MealValidator().validate(meal);
    }
}
