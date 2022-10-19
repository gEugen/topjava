package ru.javaops.topjava.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava.model.Restaurant;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.topjava.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava.util.validation.ValidationUtil.checkNew;


@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController extends AbstractRestaurantController {

    static final String REST_URL = "/api/admin/restaurant";

    @Operation(summary = "Get restaurant profile list", description = "Returns restaurant profile list")
    @GetMapping()
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll();
    }

    @Operation(summary = "Get restaurant profile by its id", description = "Returns response with found restaurant profile")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @Operation(summary = "Get restaurant profile by its e-mail", description = "Returns response with found restaurant profile")
    @GetMapping("/by-email")
    public ResponseEntity<Restaurant> getByEmail(
            @Parameter(description = "restaurant e-mail") @RequestParam String email) {
        log.info("getByEmail {}", email);
        return ResponseEntity.of(restaurantRepository.findByEmailIgnoreCase(email));
    }

    @Operation(summary = "Delete restaurant profile by its id", description = "Delete restaurant profile")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.delete(id);
    }

    @Operation(summary = "Update restaurant profile by its id", description = "Updates restaurant details")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @Parameter(description = "updated restaurant profile") @Valid @RequestBody Restaurant restaurant,
            @Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("update {}", id);
        assureIdConsistent(restaurant, id);
        restaurant.setVote(restaurantRepository.getExisted(id).getVote());
        restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Create new restaurant profile", description = "Creates new restaurant profile and returns response with it")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(
            @Parameter(description = "created restaurant profile") @Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
