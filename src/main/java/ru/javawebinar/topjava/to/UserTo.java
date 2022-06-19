package ru.javawebinar.topjava.to;

import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;

import java.util.*;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class UserTo {
    private final int id;

    private final String name;

    private final String email;

    private final String password;

    private final Date registered;

    private final Set<Role> roles;

    private final int caloriesPerDay;

    private final boolean enabled;

    public UserTo(int id, String name, String email, String password, Date registered, int caloriesPerDay, boolean enabled,
                  Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getRegistered() {
        return registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }
}