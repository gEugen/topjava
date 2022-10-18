package ru.javaops.topjava.web.vote;

import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.web.MatcherFactory;

import java.util.List;

import static ru.javaops.topjava.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_GET_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(
                    Vote.class, "voteDate", "voteTime", "restaurant.vote", "restaurant.dishes", "user.password", "user.registered");

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(
                    Vote.class, "voteDate", "voteTime", "restaurant", "user.password", "user.registered", "user.vote");

    public static final int VOTE1_ID = 1;
    public static final int VOTE2_ID = 2;
    public static final int VOTE3_ID = 3;
    public static final int VOTE4_ID = 4;
    public static final int VOTE5_ID = 5;
    public static final int VOTE6_ID = 6;
    public static final int NOT_FOUND = 100;

    public static final Vote user1Vote = new Vote(VOTE1_ID, restaurant1, user1);
    public static final Vote adminVote = new Vote(VOTE2_ID, restaurant1, admin);
    public static final Vote user2Vote = new Vote(VOTE3_ID, restaurant3, user2);
    public static final Vote user3Vote = new Vote(VOTE4_ID, restaurant3, user3);
    public static final Vote user4Vote = new Vote(VOTE5_ID, restaurant5, user4);
    public static final Vote user5Vote = new Vote(VOTE6_ID, restaurant5, user5);

    public static List<Vote> getTestCurrentVotes() {
        return List.of(user1Vote, adminVote, new Vote(VOTE4_ID, restaurant1, user3));
    }

    public static List<Vote> getTestPreviousVotes() {
        return List.of(user2Vote);
    }
}