package ru.javaops.topjava.web.restaurant;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.javaops.topjava.HasIdAndEmail;
import ru.javaops.topjava.repository.RestaurantRepository;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class RestaurantUniqueMailValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";

    private final RestaurantRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail restaurant = ((HasIdAndEmail) target);
        if (StringUtils.hasText(restaurant.getEmail())) {
            repository.findByEmailIgnoreCase(restaurant.getEmail())
                    .ifPresent(dbRestaurant -> {
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            int dbId = dbRestaurant.id();

                            // it is ok, if update ourself
                            if (restaurant.getId() != null && dbId == restaurant.id()) return;

                            // Workaround for update with user.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId))
                                return;
                        }
                        errors.rejectValue("email", "", EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
