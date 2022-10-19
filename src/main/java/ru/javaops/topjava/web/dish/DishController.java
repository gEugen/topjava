package ru.javaops.topjava.web.dish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava.model.Dish;
import ru.javaops.topjava.repository.DishRepository;
import ru.javaops.topjava.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.topjava.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava.util.validation.ValidationUtil.checkNew;


@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {
    static final String REST_URL = "/api/admin/restaurant/{restaurantId}/dish";

    private final DishRepository repository;
    private final DishService service;

    @Operation(summary = "Get dish list for restaurant by its id", description = "Returns dish list")
    @GetMapping()
    public List<Dish> getAllForRestaurant(@Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("get {}", restaurantId);
        return repository.getAll(restaurantId);
    }

    @Operation(summary = "Get dish for restaurant by its ides", description = "Returns response with dish")
    @GetMapping("/{dishId}")
    public ResponseEntity<Dish> get(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("get dish {} for restaurant {}", dishId, restaurantId);
        return ResponseEntity.of(repository.get(dishId, restaurantId));
    }

    @Operation(summary = "Delete dish for restaurant by its ides", description = "Deletes dish")
    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("delete {} for restaurant {}", restaurantId, dishId);
        Dish dish = repository.checkBelong(dishId, restaurantId);
        repository.delete(dish);
    }

    @Operation(
            summary = "Update dish details for restaurant by its ides",
            description = "Updates and returns response with updated dish")
    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @Parameter(description = "updated dish") @Valid @RequestBody Dish dish,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("update {} for restaurant {}", dish, restaurantId);
        assureIdConsistent(dish, dishId);
        repository.checkBelong(dishId, restaurantId);
        service.save(dish, restaurantId);
    }

    @Operation(
            summary = "Create new dish for restaurant by its id", description = "Creates new dish and returns response with new dish")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(
            @Parameter(description = "created dish") @Valid @RequestBody Dish dish,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("create {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        Dish created = service.save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{dishId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}