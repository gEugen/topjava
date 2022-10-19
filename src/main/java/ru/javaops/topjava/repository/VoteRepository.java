package ru.javaops.topjava.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Vote;

import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1")
    Vote getVote(int authUserId);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=:id")
    List<Vote> getVotesByRestaurant(int id);

    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=?1")
    void deleteByUserId(int id);
}
