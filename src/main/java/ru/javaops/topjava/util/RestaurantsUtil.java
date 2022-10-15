package ru.javaops.topjava.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.to.UserTo;

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

//    public static List<Restaurant> createList(Restaurant... restaurant) {
//        return Arrays.stream(restaurant)
//                .sorted(Comparator.comparing(Restaurant::getName).thenComparing(Restaurant::getEmail))
//                .collect(Collectors.toList());
//    }

    public static Restaurant updateFromTo(Restaurant restaurant, RestaurantTo restaurantTo) {
        restaurant.setName(restaurantTo.getName());
        restaurant.setEmail(restaurantTo.getEmail().toLowerCase());
        return restaurant;
    }
}
