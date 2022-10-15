package ru.javaops.topjava.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.UserRepository;

@Service
@AllArgsConstructor
public class VoteService {

    private final  UserRepository userRepository;

    private final  RestaurantRepository restaurantRepository;

    @Transactional
    public User saveWithVote(int authUserId, int id) {
        User user = userRepository.getExisted(authUserId);
        user.setRestaurant(restaurantRepository.getExisted(id));
        return userRepository.saveAndFlush(user);
    }

//    @Transactional
//    public void save(Restaurant restaurant) {
//        restaurantRepository.saveAndFlush(restaurant);
//    }
}
