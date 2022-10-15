package ru.javaops.topjava.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.web.AbstractControllerTest;
import ru.javaops.topjava.web.restaurant.RestaurantTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.util.RestaurantsUtil.*;
import static ru.javaops.topjava.util.Util.initializeAndUnproxy;
import static ru.javaops.topjava.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava.web.user.UserTestData.*;

public class ProfileVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileVoteController.REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getWithDishesAndVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT3_ID + "/with-dishes-and-vote"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTo(restaurant3, VOTED)));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getAllWithDishesAndVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(
                        RESTAURANT_TO_MATCHER.contentJson(
                                createTos(
                                        createTo(restaurant1, NOT_VOTED), createTo(restaurant2, NOT_VOTED),
                                        createTo(restaurant3, VOTED), createTo(restaurant4, NOT_VOTED),
                                        createTo(restaurant5, NOT_VOTED))));
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void vote() throws Exception {
        int previouslyVotedRestaurant = userRepository.findById(USER3_ID).get().getRestaurant().getId();
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_WITH_VOTES_MATCHER.assertMatch(restaurantRepository.findById(RESTAURANT1_ID).get(), RestaurantTestData.createVoted());
        // https://stackoverflow.com/a/2216603
        Restaurant unVotedRestaurant = initializeAndUnproxy(restaurantRepository.getExisted(previouslyVotedRestaurant));
        RESTAURANT_WITH_VOTES_MATCHER.assertMatch(unVotedRestaurant, RestaurantTestData.createTestUnVoted());
    }
}
