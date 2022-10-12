package ru.javaops.topjava.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "dishes", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "uk_restaurant_name"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Positive
    @Range(min = 0, max = 5000)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public Dish(Integer id, String name, double price) {
        super(id, name);
        this.price = price;
    }
}
