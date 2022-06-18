package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static <T> T checkNotMealValid(T object, Meal meal) {
        checkNotMealOwnerOrPresence(object != null, meal.getId(), meal.getUserId());
        return object;
    }

    public static <T> T checkNotMealValidById(T object, int mealId, int authUserId) {
        checkNotMealOwnerOrPresence(object != null, mealId, authUserId);
        return object;
    }

    public static void checkNotMealOwnerOrPresence(boolean owner, int mealId, int authUserId) {
        if (!owner) {
            throw new NotFoundException("Meal with id=" + mealId + " isn't present or not owned by user with id=" + authUserId);
        }
    }
}
