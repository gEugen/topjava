package ru.javaops.topjava.web.restaurant;

import ru.javaops.topjava.model.Dish;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.to.DishTo;
import ru.javaops.topjava.web.MatcherFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.topjava.web.meal.MealTestData.*;
import static ru.javaops.topjava.web.user.UserTestData.admin;
import static ru.javaops.topjava.web.user.UserTestData.user;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);
    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_VOTES_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("restaurant.dishes", "user.password", "user.registered").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });
    public static MatcherFactory.Matcher<DishTo> MEAL_TO_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;

    public static final String RESTAURANT1_MAIL = "astoria@yandex.ru";
    public static final String RESTAURANT2_MAIL = "continental@yandex.ru";
    public static final String RESTAURANT3_MAIL = "prague@gmail.com";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "ASTORIA", RESTAURANT1_MAIL);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "CONTINENTAL", RESTAURANT2_MAIL);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "PRAGUE", RESTAURANT3_MAIL);

    static {
        restaurant1.setUser(List.of(user, admin));
        restaurant2.setDishes(List.of(dish1, dish2, dish3));
    }
}
