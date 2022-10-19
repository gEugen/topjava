package ru.javaops.topjava.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.web.restaurant.RestaurantTestData.RESTAURANT5_ID;
import static ru.javaops.topjava.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava.web.vote.VoteTestData.*;


public class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminVoteController.REST_URL + '/';

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getVotesByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT5_ID + "/user-votes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_GET_MATCHER.contentJson(user4Vote, user5Vote));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/all-user-votes"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_GET_MATCHER.contentJson(user1Vote, adminVote, user2Vote, user3Vote, user4Vote, user5Vote));
    }
}
