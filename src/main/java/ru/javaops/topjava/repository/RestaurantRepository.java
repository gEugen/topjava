package ru.javaops.topjava.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r ORDER BY r.name ASC, r.email ASC")
    List<Restaurant> getAllWithDishes();

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Restaurant getWithDishes(int id);

//    //    https://stackoverflow.com/a/46013654/548473
//    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
//    Optional<Restaurant> getWithDishes(int id);

////    Optional<Restaurant> getWithDishes(int id);
//////    @Query("SELECT r FROM Restaurant r JOIN FETCH r.dishes WHERE r.id=?1")
//////    Restaurant getWithDishes(int id);
//
//    @Query("SELECT r FROM Restaurant r WHERE r.email = LOWER(:email)")
//    Optional<Restaurant> findByEmailIgnoreCase(String email);
//
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.users WHERE r.id=?1")
    Optional<Restaurant> getWithVotes(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.email = LOWER(:email)")
    Optional<Restaurant> findByEmailIgnoreCase(String email);

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"users"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> getAllWithUsersVotes();
}
