package ru.javaops.topjava.web.user;

import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.util.JsonUtil;
import ru.javaops.topjava.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;


public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "meals", "password", "vote");
    public static final int USER1_ID = 1;
    public static final int USER2_ID = 2;
    public static final int USER3_ID = 3;
    public static final int ADMIN_ID = 4;
    public static final int GUEST_ID = 5;
    public static final int USER4_ID = 6;
    public static final int USER5_ID = 7;

    public static final int NOT_FOUND = 100;

    public static final String USER1_MAIL = "user1@yandex.ru";
    public static final String USER2_MAIL = "user2@yandex.ru";
    public static final String USER3_MAIL = "user3@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String GUEST_MAIL = "guest@gmail.com";
    public static final String USER4_MAIL = "user4@yandex.ru";
    public static final String USER5_MAIL = "user5@yandex.ru";

    public static final User user1 = new User(USER1_ID, "User1", USER1_MAIL, "password1", Role.USER);
    public static final User user2 = new User(USER2_ID, "User2", USER2_MAIL, "password2", Role.USER);
    public static final User user3 = new User(USER3_ID, "User3", USER3_MAIL, "password3", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final User guest = new User(GUEST_ID, "Guest", GUEST_MAIL, "guest");
    public static final User user4 = new User(USER4_ID, "User4", USER4_MAIL, "password4", Role.USER);
    public static final User user5 = new User(USER5_ID, "User5", USER5_MAIL, "password5", Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER1_ID, "UpdatedName", USER1_MAIL, "newPass", false, new Date(), List.of(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}
