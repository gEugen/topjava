package ru.javawebinar.topjava.to;

import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;

import java.util.*;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class UserTo extends AbstractNamedEntity {

    private final String email;

    private final String password;

    private final Date registered = new Date();

    private final Set<Role> roles;

    private int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    private boolean enabled = true;

    public UserTo(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled,
                  Set<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
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