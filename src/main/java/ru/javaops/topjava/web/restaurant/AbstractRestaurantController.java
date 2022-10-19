package ru.javaops.topjava.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.topjava.repository.RestaurantRepository;


@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantUniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }
}
