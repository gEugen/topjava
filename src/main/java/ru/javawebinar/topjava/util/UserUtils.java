package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UserUtils {
    public static final List<User> users = Arrays.asList(
            new User(null, "Петров", "a.petrov@ya.ru", "123456", Role.USER),
            new User(null, "Сидоров", "a.sidorov@mail.ru", "111111", Role.USER),
            new User(null, "Иванов", "a.ivanov@gmail.com", "222222"),
            new User(null, "Павлов", "a.pavlov@hotmail.com", "333333", Role.USER, Role.ADMIN),
            new User(null, "Павлов", "s.pavlov@ya.ru", "444444", Role.USER)
    );
}
