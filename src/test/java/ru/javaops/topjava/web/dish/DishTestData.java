package ru.javaops.topjava.web.dish;

import ru.javaops.topjava.model.Dish;
import ru.javaops.topjava.web.MatcherFactory;


public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH5_ID = 5;
    public static final int DISH6_ID = 6;
    public static final int DISH7_ID = 7;
    public static final int DISH8_ID = 8;
    public static final int DISH9_ID = 9;
    public static final int DISH10_ID = 10;
    public static final int DISH11_ID = 11;

    public static final Dish dish1 = new Dish(DISH1_ID, "Escalope", 2.5);
    public static final Dish dish2 = new Dish(DISH2_ID, "Grilled chicken", 1.1);
    public static final Dish dish3 = new Dish(DISH3_ID, "Marinated squid", 0.5);
    public static final Dish dish4 = new Dish(DISH4_ID, "Scrambled eggs", 0.9);
    public static final Dish dish5 = new Dish(DISH5_ID, "Vegetable stew", 1.05);
    public static final Dish dish6 = new Dish(DISH6_ID, "Italian pasta", 0.6);
    public static final Dish dish7 = new Dish(DISH7_ID, "Sponge cake", 3.1);
    public static final Dish dish8 = new Dish(DISH8_ID, "Coconut ice cream", 2.1);
    public static final Dish dish9 = new Dish(DISH9_ID, "Coffee with milk", 0.25);
    public static final Dish dish10 = new Dish(DISH10_ID, "Coffee", 0.1);
    public static final Dish dish11 = new Dish(DISH11_ID, "Tea", 0.05);

    public static Dish getNew() {
        return new Dish(null, "Watson soup", 4.09);
    }

    public static Dish getUpdated() {
        return new Dish(DISH4_ID, "Holmes oatmeal", 5.5);
    }
}
