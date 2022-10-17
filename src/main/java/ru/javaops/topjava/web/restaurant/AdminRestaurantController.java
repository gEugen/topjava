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

    @Operation(summary = "Get list of restaurants", description = "Returns list of restaurants")
    @GetMapping()
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll();
    }

    @Operation(summary = "Get restaurant by its id", description = "Returns response with found restaurant")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@Parameter(description = "id of selected restaurant") @PathVariable int id) {
        log.info("get {}", id);
        return super.get(id);
    }

    @Operation(summary = "Get restaurant by its e-mail", description = "Returns response with found restaurant")
    @GetMapping("/by-email")
    public ResponseEntity<Restaurant> getByEmail(
            @Parameter(description = "e-mail for getting restaurant") @RequestParam String email) {
        log.info("getByEmail {}", email);
        return ResponseEntity.of(restaurantRepository.findByEmailIgnoreCase(email));
    }

    @Operation(summary = "Delete restaurant by its id", description = "Delete selected restaurant")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "id of selected restaurant") @PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.delete(id);
    }

    @Operation(summary = "Update restaurant details", description = "Updates restaurant details")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @Parameter(description = "restaurant with updated details") @Valid @RequestBody Restaurant restaurant,
            @Parameter(description = "id of selected restaurant") @PathVariable int id) {
        log.info("update {}", id);
        assureIdConsistent(restaurant, id);
        restaurant.setUsers(restaurantRepository.getExisted(id).getUsers());
        restaurantRepository.save(restaurant);
    }

    @Operation(
            summary = "Create new restaurant with details",
            description = "Creates new restaurant and returns response with it and its details")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(
            @Parameter(description = "created restaurant with details") @Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
