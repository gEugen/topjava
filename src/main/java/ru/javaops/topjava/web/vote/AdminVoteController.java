package ru.javaops.topjava.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.repository.VoteRepository;

import java.util.List;


@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminVoteController {

    static final String REST_URL = "/api/admin/vote/restaurant";

    private final VoteRepository voteRepository;

    @Operation(summary = "Get user votes for selected restaurant", description = "Returns user votes for selected restaurant")
    @GetMapping("/{id}/user-votes")
    public List<Vote> getVotesByRestaurant(@Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("getWithUsersVotes");
        return voteRepository.getVotesByRestaurant(id);
    }

    @Operation(summary = "Get all user vote list", description = "Returns all user vote list")
    @GetMapping("/all-user-votes")
    public List<Vote> getAllVotes() {
        log.info("getAllWithUsersVotes");
        return voteRepository.findAll();
    }
}
