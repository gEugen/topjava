package ru.javaops.topjava.web.restaurant;

import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.web.MatcherFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.topjava.web.dish.DishTestData.*;
import static ru.javaops.topjava.web.user.UserTestData.*;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class);

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_VOTES_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes", "users.password", "users.registered", "users.restaurant").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_UPDATE_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes.restaurant", "users.password", "users.registered",
                                    "users.restaurant").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_GET_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes", "users").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT4_ID = 4;
    public static final int NOT_FOUND = 200;

    public static final String RESTAURANT1_MAIL = "astoria@yandex.ru";
    public static final String RESTAURANT2_MAIL = "continental@yandex.ru";
    public static final String RESTAURANT3_MAIL = "prague@gmail.com";
    public static final String RESTAURANT4_MAIL = "sushibar@gmail.com";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "ASTORIA", RESTAURANT1_MAIL);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "CONTINENTAL", RESTAURANT2_MAIL);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "PRAGUE", RESTAURANT3_MAIL);
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT4_ID, "SUSHI BAR", RESTAURANT4_MAIL);

    public static final List<Restaurant> restaurants =
            List.of(new Restaurant(restaurant1), new Restaurant(restaurant2), new Restaurant(restaurant3), new Restaurant(restaurant4));

    static {
        restaurant1.setUsers(List.of(user1, admin));
        restaurant1.setDishes(List.of(dish1, dish2, dish3));

        restaurant2.setDishes(List.of(dish4, dish5, dish6));

        restaurant3.setUsers(List.of(user2, user3));
        restaurant3.setDishes(List.of(dish7, dish8, dish9));
    }

    public static Restaurant createVoted() {
        restaurant1.setUsers(List.of(user1, user3, admin));
        return restaurant1;
    }

    public static Restaurant createTestUnVoted() {
        restaurant3.setUsers(List.of(user2));
        return restaurant3;
    }

    public static Restaurant getUpdated() {
        Restaurant updatedRestaurant = new Restaurant(restaurant1);
        updatedRestaurant.setName("ASTORIA_NEW");
        updatedRestaurant.setEmail("astoria_new@yandex.ru");
        return updatedRestaurant;
    }

    public static Restaurant getUpdatedForCompare() {
        Restaurant updatedForCompare = new Restaurant(getUpdated());
        updatedForCompare.setUsers(List.of(user1, admin));
        return updatedForCompare;
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "NEW_RESTAURANT", "new_restaurant@mail.ru");
    }
}
