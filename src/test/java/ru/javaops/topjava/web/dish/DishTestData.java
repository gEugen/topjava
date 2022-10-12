package ru.javaops.topjava.web.dish;

import ru.javaops.topjava.model.Dish;
import ru.javaops.topjava.to.DishTo;
import ru.javaops.topjava.web.MatcherFactory;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static MatcherFactory.Matcher<DishTo> MEAL_TO_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);

    public static final int DISH1_ID = 4;
    public static final int DISH2_ID = 5;
    public static final int DISH3_ID = 6;

    public static final Dish dish1 = new Dish(DISH1_ID, "Scrambled eggs", 0.9);
    public static final Dish dish2 = new Dish(DISH2_ID, "Vegetable stew", 1.05);
    public static final Dish dish3 = new Dish(DISH3_ID, "Italian pasta", 0.6);

//    public static final Meal meal1 = new Meal(MEAL1_ID, of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
//    public static final Meal meal2 = new Meal(MEAL1_ID + 1, of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
//    public static final Meal meal3 = new Meal(MEAL1_ID + 2, of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
//    public static final Meal meal4 = new Meal(MEAL1_ID + 3, of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
//    public static final Meal meal5 = new Meal(MEAL1_ID + 4, of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 500);
//    public static final Meal meal6 = new Meal(MEAL1_ID + 5, of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000);
//    public static final Meal meal7 = new Meal(MEAL1_ID + 6, of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 510);
//    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_ID, of(2020, Month.JANUARY, 31, 14, 0), "Админ ланч", 510);
//    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL_ID + 1, of(2020, Month.JANUARY, 31, 21, 0), "Админ ужин", 1500);
//
//    public static final List<Meal> meals = List.of(meal7, meal6, meal5, meal4, meal3, meal2, meal1);
//
    public static Dish getNew() {
        return new Dish(null, "Watson soup", 4.09);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Holmes oatmeal", 5.5);
    }
}
