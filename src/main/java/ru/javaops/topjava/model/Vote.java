package ru.javaops.topjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javaops.topjava.util.DateTimeUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}, name = "uk_user_date"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    public static LocalTime END_VOTE_TIME = LocalTime.parse("11:00");

    @Column(name = "date", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @NotNull
    @DateTimeFormat(pattern = DateTimeUtil.DATE_PATTERN)
    private LocalDate voteDate;

    @Column(name = "time", nullable = false, columnDefinition = "TIME DEFAULT CURRENT_TIME")
    @NotNull
    @DateTimeFormat(pattern = DateTimeUtil.TIME_PATTERN)
    private LocalTime voteTime;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @Schema(hidden = true)
    private Restaurant restaurant;

    @OneToOne
    @JoinColumn(name = "user_id")
    @Schema(hidden = true)
    private User user;

    public Vote(int id, Restaurant restaurant, User user) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
    }

    public Vote(Integer id, LocalDate voteDate, LocalTime voteTime, Restaurant restaurant, User user) {
        super(id);
        this.voteDate = voteDate;
        this.voteTime = voteTime;
        this.restaurant = restaurant;
        this.user = user;
    }

    public Vote(Vote vote) {
        this(vote.id, vote.voteDate, vote.voteTime, vote.restaurant, vote.user);
    }
}
