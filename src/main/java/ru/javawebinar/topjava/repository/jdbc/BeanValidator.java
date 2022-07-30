package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidator {
    private static final Logger log = LoggerFactory.getLogger(BeanValidator.class);
    private final ValidatorFactory factory;
    private final Validator validator;


    public BeanValidator() {
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public <T> int validate(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        for (ConstraintViolation<T> violation : violations) {
            log.error(violation.getMessage());
        }
        return violations.size();
    }
}
