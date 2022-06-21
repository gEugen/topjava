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

    public static <T> void checkNotValidResult(T object, Meal meal) {
        checkNotOwnerOrPresence(object != null, meal.getId(), meal.getUserId());
    }

    public static <T> T checkNotValidResultById(T object, int mealId, int userId) {
        checkNotOwnerOrPresence(object != null, mealId, userId);
        return object;
    }

    public static void checkNotOwnerOrPresence(boolean valid, int mealId, int userId) {
        if (!valid) {
            throw new NotFoundException("Meal with id=" + mealId + " isn't present or not owned by user with id=" + userId);
        }
    }

    public static void checkNotValidAuthUserId(Meal meal, Integer userId) {
        if (userId == null) {
            throw new NotFoundException(meal + "must be with userId != null");
        }
    }
}
