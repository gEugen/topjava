package ru.javaops.topjava.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava.model.Dish;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.to.RestaurantTo;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.topjava.util.RestaurantsUtil.updateFromTo;
import static ru.javaops.topjava.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController extends AbstractRestaurantController {

    static final String REST_URL = "/api/admin/restaurant";

    @GetMapping()
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return super.get(id);
    }

//    @GetMapping("/{id}/get-with-dishes")
//    public ResponseEntity<Restaurant> getWithDishes(@PathVariable int id) {
//        log.info("get {}", id);
//        return ResponseEntity.of(restaurantRepository.getWithDishes(id));
//    }

    @GetMapping("/by-email")
    public ResponseEntity<Restaurant> getByEmail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return ResponseEntity.of(restaurantRepository.findByEmailIgnoreCase(email));
    }

    @GetMapping("/{id}/with-votes")
    public Restaurant getWithUsersVotes(@PathVariable int id) {
        log.info("getAllWithUsersVotes");
        return restaurantRepository.getWithVotes(id).get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.delete(id);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", id);
        assureIdConsistent(restaurant, id);
        restaurant.setUsers(restaurantRepository.getExisted(id).getUsers());
        restaurantRepository.save(restaurant);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
