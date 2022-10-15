package ru.javaops.topjava.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.web.AuthUser;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.topjava.util.RestaurantsUtil.createTo;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileVoteController extends AbstractVoteController {

    static final String REST_URL = "/api/profile/vote/restaurant";

//    @GetMapping("/{id}/with-votes")
//    public ResponseEntity<Restaurant> getWithVotes(@PathVariable int id) {
//        return ResponseEntity.of(restaurantRepository1.getWithVotes(id));
//    }

//    @GetMapping("/{id}/with-dishes")
//    public ResponseEntity<RestaurantTo> getWithDishesAndVote(@PathVariable int id) {
//        return super.getWithDishesAndVote(id);
//    }

    @GetMapping("/{id}/with-dishes-and-vote")
    public RestaurantTo getWithDishesAndVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("getWithDishesAndVote {} for user {}", id, authUserId);
//        Restaurant restaurant = super.get(id);
        Restaurant restaurant = restaurantRepository.getWithDishes(id);
        Integer votedRestaurantId = super.getVotedRestaurant(authUserId).getId();
        RestaurantTo restaurantTo;
        if (votedRestaurantId != null && votedRestaurantId == id) {
            restaurantTo = createTo(restaurant, true);
        } else {
            restaurantTo = createTo(restaurant, false);
        }
        return restaurantTo;
    }

    @GetMapping()
    public List<RestaurantTo> getAllWithDishesAndVote(@AuthenticationPrincipal AuthUser authUser) {
        int authUserId = authUser.id();
        log.info("getAllWithDishesAndVote for user {}", authUserId);
        List<Restaurant> restaurants = restaurantRepository.getAllWithDishes();
        Integer votedRestaurantId = super.getVotedRestaurant(authUserId).getId();
        List<RestaurantTo> restaurantToList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (votedRestaurantId != null && votedRestaurantId.equals(restaurant.getId())) {
                restaurantToList.add(createTo(restaurant, true));
            } else {
                restaurantToList.add(createTo(restaurant, false));
            }
        }
        return restaurantToList;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("vote {} by user {}", id, authUserId);
        voteService.saveWithVote(authUserId, id);
    }
}
