package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasLength;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(), admin, guest);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void registerWithNonValidData() throws Exception {
        UserTo newTo = new UserTo(null, "", "newemail ya.ru", "n", 0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().string(hasLength(294)))
                .andExpect(content().string(containsString("http://localhost/rest/profile")))
                .andExpect(content().string(containsString("VALIDATION_ERROR")))
                .andExpect(content().string(containsString("[password] length must be between 5 and 32 characters")))
                .andExpect(content().string(containsString("[email] must be a well-formed email address")))
                .andExpect(content().string(containsString("[caloriesPerDay] must be between 10 and 10000")))
                .andExpect(content().string(containsString("[name] size must be between 2 and 100")))
                .andExpect(content().string(containsString("[name] must not be blank")));

        perform(MockMvcRequestBuilders.get(AdminRestController.REST_URL + '/')
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, guest, user));
    }

    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "user@yandex.ru", "newPassword", 1500);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    void updateWithNonValidData() throws Exception {
        UserTo updatedTo = new UserTo(null, "n", "", "", 2000);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().string(hasLength(232)))
                .andExpect(content().string(containsString("http://localhost/rest/profile")))
                .andExpect(content().string(containsString("VALIDATION_ERROR")))
                .andExpect(content().string(containsString("[password] length must be between 5 and 32 characters")))
                .andExpect(content().string(containsString("[email] must not be blank")))
                .andExpect(content().string(containsString("[name] size must be between 2 and 100")))
                .andExpect(content().string(containsString("[password] must not be blank")));

        perform(MockMvcRequestBuilders.get(AdminRestController.REST_URL + '/')
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, guest, user));
    }

    @Test
    void getWithMeals() throws Exception {
        assumeDataJpa();
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-meals")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_MEALS_MATCHER.contentJson(user));
    }
}