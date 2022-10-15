package ru.javaops.topjava.util;

import ru.javaops.topjava.model.User;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UsersUtil {
    public static List<User> getOrderedList(User... users) {
        return Arrays.stream(users)
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }
}
