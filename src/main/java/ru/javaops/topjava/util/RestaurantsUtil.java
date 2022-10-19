package ru.javaops.topjava.util;

import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.to.RestaurantTo;

import java.util.*;
import java.util.stream.Collectors;

public class RestaurantsUtil {

    public static RestaurantTo createTo(Restaurant restaurant, boolean vote) {
        return new RestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getEmail(),
                Optional.ofNullable(restaurant.getDishes()).orElse(new ArrayList<>()),
                vote);
    }

    public static List<RestaurantTo> createTos(RestaurantTo... restaurantTos) {
        return Arrays.stream(restaurantTos)
                .sorted(Comparator.comparing(RestaurantTo::getName).thenComparing(RestaurantTo::getEmail))
                .collect(Collectors.toList());
    }
}
