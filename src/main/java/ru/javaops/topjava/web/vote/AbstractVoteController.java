package ru.javaops.topjava.web.vote;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.repository.VoteRepository;
import ru.javaops.topjava.service.VoteService;

public abstract class AbstractVoteController {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected VoteService voteService;

    @Autowired
    protected VoteRepository voteRepository;
}
