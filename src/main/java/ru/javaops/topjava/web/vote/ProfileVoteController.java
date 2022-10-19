package ru.javaops.topjava.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.VoteRepository;
import ru.javaops.topjava.service.VoteService;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.web.AuthUser;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.topjava.util.RestaurantsUtil.createTo;


@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileVoteController {

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    static final String REST_URL = "/api/profile/vote/restaurant";

    private final VoteService voteService;

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get restaurant with vote mark by user", description = "Returns restaurant with vote mark")
    @GetMapping("/{id}/with-dishes-and-vote")
    public RestaurantTo getWithVoteMark(
            @AuthenticationPrincipal AuthUser authUser, @Parameter(description = "id of restaurant") @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("getWithDishesAndVote {} for user {}", id, authUserId);
        Restaurant restaurant = restaurantRepository.getWithDishes(id);
        Integer votedRestaurantId = voteRepository.getVote(authUserId).getRestaurant().getId();
        RestaurantTo restaurantTo;
        if (votedRestaurantId != null && votedRestaurantId == id) {
            restaurantTo = createTo(restaurant, VOTED);
        } else {
            restaurantTo = createTo(restaurant, NOT_VOTED);
        }
        return restaurantTo;
    }

    @Operation(
            summary = "Get restaurant list with vote marks for each one by user",
            description = "Returns restaurant list with vote marks")
    @GetMapping()
    public List<RestaurantTo> getAllWithVoteMark(@AuthenticationPrincipal AuthUser authUser) {
        int authUserId = authUser.id();
        log.info("getAllWithDishesAndVote for user {}", authUserId);
        List<Restaurant> restaurants = restaurantRepository.getAllWithDishes();
        Integer votedRestaurantId = voteRepository.getVote(authUserId).getRestaurant().getId();
        List<RestaurantTo> restaurantToList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (votedRestaurantId != null && votedRestaurantId.equals(restaurant.getId())) {
                restaurantToList.add(createTo(restaurant, VOTED));
            } else {
                restaurantToList.add(createTo(restaurant, NOT_VOTED));
            }
        }
        return restaurantToList;
    }

    @Operation(summary = "Vote for restaurant selected by user", description = "Marks restaurant as voted selected by user")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@AuthenticationPrincipal AuthUser authUser, @Parameter(description = "id of restaurant") @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("vote {} by user {}", id, authUserId);
        voteService.saveWithVote(authUserId, id);
    }
}
