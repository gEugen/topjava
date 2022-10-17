package ru.javaops.topjava.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.topjava.model.Restaurant;

import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminVoteController extends AbstractVoteController {

    static final String REST_URL = "/api/admin/vote/restaurant";

    @Operation(
            summary = "Get a restaurant with list of voted users",
            description = "Returns a restaurant with list of users who have voted it")
    @GetMapping("/{id}/with-user-votes")
    public ResponseEntity<Restaurant> getWithUsersVotes(@Parameter(description = "id of restaurant for getting result") @PathVariable int id) {
        log.info("getWithUsersVotes");
        return ResponseEntity.of(restaurantRepository.getWithVotes(id));
    }

    @Operation(
            summary = "Get a list of restaurants with lists of voted users",
            description = "Returns list of restaurants with lists of users who have voted them")
    @GetMapping("/with-user-votes")
    public List<Restaurant> getAllWithUsersVotes() {
        log.info("getAllWithUsersVotes");
        return restaurantRepository.getAllWithUsersVotes();
    }
}
