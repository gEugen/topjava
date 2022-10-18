package ru.javaops.topjava.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static ru.javaops.topjava.model.Vote.END_VOTE_TIME;
import static ru.javaops.topjava.util.DateTimeUtil.DATE_FORMATTER;
import static ru.javaops.topjava.util.DateTimeUtil.TIME_FORMATTER;

@Service
@AllArgsConstructor
public class VoteService {

    private final  UserRepository userRepository;

    private final  RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    @Transactional
    public void saveWithVote(int authUserId, int id) {
        Vote vote = voteRepository.getVote(authUserId);
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        if (time.isBefore(END_VOTE_TIME)) {
            if (vote == null || vote.getVoteDate().isBefore(date)) {
                voteRepository.saveAndFlush(
                        new Vote(null, date, time, restaurantRepository.getReferenceById(id), userRepository.getReferenceById(authUserId)));
            } else {
                voteRepository.saveAndFlush(
                        new Vote(vote.getId(), date, time, restaurantRepository.getReferenceById(id), userRepository.getReferenceById(authUserId)));
            }
        }
    }
}
