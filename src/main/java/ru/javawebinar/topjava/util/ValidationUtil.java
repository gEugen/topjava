package ru.javawebinar.topjava.util;


import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

public class ValidationUtil {

    public static final Locale LOCALE_RU = new Locale("ru", "RU");

    public static final String EMAIL_DUPLICATION = "users_unique_email_idx";

    public static final String DATE_TIME_DUPLICATION = "meals_unique_user_datetime_idx";

    public static final String DUPLICATION_MESSAGE = "common.duplication";

    public static final Object[] EN_FULL_MAIL_DUPLICATION_ARGS = new Object[]{"[email] ", "User", "email"};

    public static final Object[] EN_MAIL_DUPLICATION_ARGS = new Object[]{"", "User", "email"};

    public static final Object[] RU_FULL_MAIL_DUPLICATION_ARGS = new Object[]{"[email] ", "Пользователь", "адресом"};

    public static final Object[] RU_MAIL_DUPLICATION_ARGS = new Object[]{"", "Пользователь", "адресом"};

    public static final Object[] EN_FULL_DATE_TIME_DUPLICATIONS_ARGS = new Object[]{"[dateTime] ", "Meal", "date/time"};

    public static final Object[] RU_FULL_DATE_TIME_DUPLICATIONS_ARGS = new Object[]{"[dateTime] ", "Еда", "датой/временем"};

    private static final Validator validator;

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();
    }

    private ValidationUtil() {
    }

    public static <T> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

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

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static String getDefaultMessage(final Locale locale, final String key) {
        PlatformResourceBundleLocator bundleLocator = new PlatformResourceBundleLocator("org.hibernate.validator.ValidationMessages");
        ResourceBundle resourceBundle = bundleLocator.getResourceBundle(locale);
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String[] getMessageDetails(MessageSource source, String duplicationIdentifier) {
        String[] details = new String[1];
        Object[] args = getArgs(duplicationIdentifier, true);
        details[0] = new MessageSourceAccessor(source).getMessage(DUPLICATION_MESSAGE, args, LocaleContextHolder.getLocale());
        return details;
    }

    public static Object[] getArgs(String duplicationIdentifier, boolean fullArgs) {
        Object[] args = null;
        if ("ru".equals(LocaleContextHolder.getLocale().toString())) {
            if (EMAIL_DUPLICATION.equals(duplicationIdentifier)) {
                args = fullArgs ? RU_FULL_MAIL_DUPLICATION_ARGS : RU_MAIL_DUPLICATION_ARGS;
            } else if (DATE_TIME_DUPLICATION.equals(duplicationIdentifier)) {
                args = RU_FULL_DATE_TIME_DUPLICATIONS_ARGS;
            }
        } else if ("en".equals(LocaleContextHolder.getLocale().toString())) {
            if (EMAIL_DUPLICATION.equals(duplicationIdentifier)) {
                args = fullArgs ? EN_FULL_MAIL_DUPLICATION_ARGS : EN_MAIL_DUPLICATION_ARGS;
            } else if (DATE_TIME_DUPLICATION.equals(duplicationIdentifier)) {
                args = EN_FULL_DATE_TIME_DUPLICATIONS_ARGS;
            }
        }
        return args;
    }
}