package ru.javaops.topjava.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.repository.DishRepository;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.service.VoteService;
import ru.javaops.topjava.web.user.UniqueMailValidator;

import java.util.List;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected VoteService voteService;

    @Autowired
    protected DishRepository dishRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

//    public ResponseEntity<RestaurantTo> getWithDishesAndVote(int id) {
//        log.info("getWithDishes {}", id);
//        return ResponseEntity.of(createTo(restaurantRepository.getWithDishes(id).get(),
//                Optional< User >userRepository.getWithRestaurant(id).isPresent()));
//    }

//    public RestaurantTo getWithDishesAndVote(int id) {
//        log.info("getWithDishes {}", id);
//        RestaurantTo restaurant = createTo(restaurantRepository.getWithDishes(id),
//                userRepository.getWithRestaurant(id).isPresent());
//        return restaurant;
//    }

    public ResponseEntity<Restaurant> get(int id) {
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }
}
