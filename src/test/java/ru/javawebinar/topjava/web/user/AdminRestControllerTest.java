package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.util.ValidationUtil.LOCALE_RU;
import static ru.javawebinar.topjava.util.ValidationUtil.getDefaultMessage;

class AdminRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-email?email=" + user.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), updated);
    }

    @Test
    void updateWithNonValidData() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(getUpdatedWithNonValidData(), user.getPassword())))
                .andExpect(status().is(422))
                .andExpect(content().string(containsString("http://localhost/rest/admin/users/" + USER_ID)))
                .andExpect(content().string(containsString("VALIDATION_ERROR")))
                .andExpect(content().string(containsString("[name] " + getDefaultMessage(LOCALE_RU, "javax.validation.constraints.NotBlank.message"))))
                .andExpect(content().string(containsString("[email] " + getDefaultMessage(LOCALE_RU, "javax.validation.constraints.NotBlank.message"))));

        USER_MATCHER.assertMatch(userService.get(USER_ID), user);
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void createWithNonValidData() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(getNewWithNonValidData(), user.getPassword())))
                .andExpect(status().is(422))
                .andExpect(content().string(containsString("http://localhost/rest/admin/users/")))
                .andExpect(content().string(containsString("VALIDATION_ERROR")))
                .andExpect(content().string(containsString("[name] " + getDefaultMessage(LOCALE_RU, "javax.validation.constraints.Size.message").replace("{min}", "2").replace("{max}", "128"))))
                .andExpect(content().string(containsString("[email] " + getDefaultMessage(LOCALE_RU, "javax.validation.constraints.Email.message"))));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithExistingEmail() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(getNewWithExistingEmail(), user.getPassword())))
                .andExpect(status().is(409))
                .andExpect(content().string(containsString("http://localhost/rest/admin/users/")))
                .andExpect(content().string(containsString("VALIDATION_ERROR")))
                .andExpect(content().string(containsString("[email] " + messageSource.getMessage("common.users_unique_email_idx", null, LOCALE_RU))));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, guest, user));
    }

    @Test
    void getWithMeals() throws Exception {
        assumeDataJpa();
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID + "/with-meals")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_MEALS_MATCHER.contentJson(admin));
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userService.get(USER_ID).isEnabled());
    }
}