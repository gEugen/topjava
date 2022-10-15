package ru.javaops.topjava.web.vote;

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

    @GetMapping("/{id}/with-user-votes")
    public ResponseEntity<Restaurant> getWithUsersVotes(@PathVariable int id) {
        log.info("getWithUsersVotes");
        return ResponseEntity.of(restaurantRepository.getWithVotes(id));
    }

    @GetMapping("/with-user-votes")
    public List<Restaurant> getAllWithUsersVotes() {
        log.info("getAllWithUsersVotes");
        return restaurantRepository.getAllWithUsersVotes();
    }
}
