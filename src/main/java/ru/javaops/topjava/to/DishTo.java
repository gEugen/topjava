package ru.javaops.topjava.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends BaseTo {

    double price;

    public DishTo(Integer id, double price) {
        super(id);
        this.price = price;
    }
}
