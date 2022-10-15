package ru.javaops.topjava.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends BaseTo {

    @NotNull
    @Positive
    @Range(min = 0, max = 5000)
    double price;

    public DishTo(Integer id, double price) {
        super(id);
        this.price = price;
    }
}
