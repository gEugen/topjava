package ru.javawebinar.topjava.optional;

public class IdGenerator {
    private static Integer id = 0;

    public static synchronized Integer getId() {
        return ++id;
    }
}
