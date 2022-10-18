package ru.javaops.topjava.web.restaurant;

import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.web.MatcherFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.topjava.web.dish.DishTestData.*;
import static ru.javaops.topjava.web.user.UserTestData.*;
import static ru.javaops.topjava.web.vote.VoteTestData.*;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class);

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_VOTES_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("dishes", "vote.voteDate", "vote.voteTime", "vote.restaurant", "vote.user" +
                                            ".password", "vote.user.registered",
                                    "vote.user.restaurant", "vote.user.vote").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_UPDATE_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields(
                                    "vote.voteDate", "vote.voteTime", "vote.restaurant.vote", "vote.user.password",
                                    "vote.user.registered", "vote.user.vote")
                            .isEqualTo(e),
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

//    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_GET_VOTES_MATCHER =
//            MatcherFactory.usingAssertions(Restaurant.class,
//                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
//                    (a, e) -> assertThat(a).usingRecursiveComparison()
//                            .ignoringFields("users").isEqualTo(e),
//                    (a, e) -> {
//                        throw new UnsupportedOperationException();
//                    });

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_GET_VOTES_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "users");

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT4_ID = 4;
    public static final int RESTAURANT5_ID = 5;
    public static final int NOT_FOUND = 200;

    public static final String RESTAURANT1_MAIL = "astoria@yandex.ru";
    public static final String RESTAURANT2_MAIL = "continental@yandex.ru";
    public static final String RESTAURANT3_MAIL = "prague@gmail.com";
    public static final String RESTAURANT4_MAIL = "sushibar@gmail.com";
    public static final String RESTAURANT5_MAIL = "niam-niam@gmail.com";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "ASTORIA", RESTAURANT1_MAIL);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "CONTINENTAL", RESTAURANT2_MAIL);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "PRAGUE", RESTAURANT3_MAIL);
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT4_ID, "SUSHI BAR", RESTAURANT4_MAIL);
    public static final Restaurant restaurant5 = new Restaurant(RESTAURANT5_ID, "NIAM-NIAM", RESTAURANT5_MAIL);

    public static final List<Restaurant> restaurants;
    public static final List<Restaurant> restaurantsWithUserVotes;

    static {
        restaurants = getListOfRestaurants();

//        restaurant1.setUsers(List.of(user1, admin));
//        restaurant2.setUsers(new ArrayList<>());
//        restaurant3.setUsers(List.of(user2, user3));
//        restaurant4.setUsers(new ArrayList<>());
//        restaurant5.setVote(List.of(user4Vote, user5Vote));

        restaurantsWithUserVotes = getListOfRestaurants();

        restaurant1.setDishes(List.of(dish1, dish2, dish3));
        restaurant2.setDishes(List.of(dish4, dish5, dish6));
        restaurant3.setDishes(List.of(dish7, dish8, dish9));
        restaurant5.setDishes(List.of(dish10, dish11));
    }

    public static Restaurant createVoted() {
        Restaurant voted = new Restaurant(restaurant1);
        voted.setVote(List.of(user1Vote, adminVote, user3Vote));
        return voted;
    }

    public static Restaurant createTestUnVoted() {
        restaurant3.setVote(List.of(user2Vote));
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
        Vote updatedVote1 = new Vote(user1Vote);
        Vote updatedVote2 = new Vote(adminVote);
        updatedVote1.setRestaurant(getUpdated());
        updatedVote2.setRestaurant(getUpdated());
        updatedForCompare.setVote(List.of(updatedVote1, updatedVote2));
        return updatedForCompare;
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "NEW_RESTAURANT", "new_restaurant@mail.ru");
    }

    public static List<Restaurant> getListOfRestaurants() {
        return List.of(
                new Restaurant(restaurant1),
                new Restaurant(restaurant2),
                new Restaurant(restaurant3),
                new Restaurant(restaurant4),
                new Restaurant(restaurant5));
    }
}
