package ru.javaops.topjava.web.dish;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Dish;
import ru.javaops.topjava.repository.DishRepository;
import ru.javaops.topjava.util.JsonUtil;
import ru.javaops.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.web.dish.DishTestData.*;
import static ru.javaops.topjava.web.restaurant.RestaurantTestData.*;
import static ru.javaops.topjava.web.user.UserTestData.ADMIN_MAIL;


class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishController.REST_URL + '/';

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, RESTAURANT5_ID))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish10, dish11));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH11_ID, RESTAURANT5_ID))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish11));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH4_ID, RESTAURANT2_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.get(RESTAURANT2_ID, DISH4_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH4_ID, RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(repository.getExisted(DISH4_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)));

        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.getExisted(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID, RESTAURANT3_ID))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUnauth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID, RESTAURANT2_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, "", 200);
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH1_ID, "Scrambled eggs", 0);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID, RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Dish invalid = new Dish(DISH1_ID, "<script>alert(123)</script>", 200);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID, RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() {
        Dish invalid = new Dish(DISH5_ID, "Scrambled eggs", 0.9);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.put(REST_URL + DISH5_ID, RESTAURANT2_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
        );
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() {
        Dish invalid = new Dish(null, "Scrambled eggs", 0.9);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT2_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity())
        );
    }
//
//    @Test
//    @WithUserDetails(value = USER_MAIL)
//    void get() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MEAL_MATCHER.contentJson(meal1));
//    }
//
//    @Test
//    @WithUserDetails(value = USER_MAIL)
//    void getNotFound() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_MEAL_ID))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @WithUserDetails(value = USER_MAIL)
//    void getAll() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(meals, UserTestData.user.getCaloriesPerDay())));
//    }
}