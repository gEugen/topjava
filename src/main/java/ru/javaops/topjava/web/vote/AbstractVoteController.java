package ru.javaops.topjava.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.service.VoteService;

@Slf4j
public abstract class AbstractVoteController {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected VoteService voteService;

    public Restaurant getVotedRestaurant(int id) {
        return userRepository.findById(id).get().getRestaurant();
    }
}
