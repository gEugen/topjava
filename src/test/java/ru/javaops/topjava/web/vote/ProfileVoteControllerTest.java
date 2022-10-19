package ru.javaops.topjava.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.repository.VoteRepository;
import ru.javaops.topjava.web.AbstractControllerTest;

import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.model.Vote.END_VOTE_TIME;
import static ru.javaops.topjava.util.DateTimeUtil.TIME_FORMATTER;
import static ru.javaops.topjava.util.RestaurantsUtil.createTo;
import static ru.javaops.topjava.util.RestaurantsUtil.createTos;
import static ru.javaops.topjava.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava.web.user.UserTestData.USER2_MAIL;
import static ru.javaops.topjava.web.user.UserTestData.USER3_MAIL;
import static ru.javaops.topjava.web.vote.VoteTestData.*;


public class ProfileVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileVoteController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getWithVoteMark() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT3_ID + "/with-dishes-and-vote"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTo(restaurant3, VOTED)));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getAllWithVoteMark() throws Exception {
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
    void validVote() throws Exception {
        END_VOTE_TIME = LocalTime.parse(LocalTime.MAX.format(TIME_FORMATTER));
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        List<Vote> currentVotes = voteRepository.getVotesByRestaurant(RESTAURANT1_ID);
        List<Vote> previousVotes = voteRepository.getVotesByRestaurant(RESTAURANT3_ID);
        VOTE_MATCHER.assertMatch(currentVotes, getTestCurrentVotes());
        VOTE_MATCHER.assertMatch(previousVotes, getTestPreviousVotes());
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void nonValidVote() throws Exception {
        END_VOTE_TIME = LocalTime.parse(LocalTime.MIN.format(TIME_FORMATTER));
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        List<Vote> currentVotes = voteRepository.getVotesByRestaurant(RESTAURANT1_ID);
        List<Vote> previousVotes = voteRepository.getVotesByRestaurant(RESTAURANT3_ID);
        VOTE_MATCHER.assertMatch(currentVotes, List.of(user1Vote, adminVote));
        VOTE_MATCHER.assertMatch(previousVotes, List.of(user2Vote, user3Vote));
    }
}