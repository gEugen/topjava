package ru.javaops.topjava.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Restaurant;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> getWithDishes(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.email = LOWER(:email)")
    Optional<Restaurant> findByEmailIgnoreCase(String email);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.user WHERE r.id=?1")
    Optional<Restaurant> getWithVotes(int id);
}
