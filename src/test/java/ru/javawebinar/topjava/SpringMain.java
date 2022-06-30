package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpringMain {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger(SpringMain.class);
        try {
            log.debug("initialize database");
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/topjava", "user", "password");
            Statement stmt = conn.createStatement();

            String[] sqlScripts = {"src//main//resources//db//initDB.sql", "src//main//resources//db" +
                    "//populateDB.sql"};

            for (String script :
                    sqlScripts) {
                FileReader fileReader = new FileReader(script);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                log.debug("run script {}", script);
                stmt.execute(bufferedReader.lines().collect(Collectors.joining()));
            }

            stmt.close();
            conn.close();
            log.debug("database initialized");

            // java 7 automatic resource management (ARM)
            try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")) {
                System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
                AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
                adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
                System.out.println();

                MealRestController mealController = appCtx.getBean(MealRestController.class);
                List<MealTo> filteredMealsWithExcess =
                        mealController.getBetween(
                                LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
                                LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
                filteredMealsWithExcess.forEach(System.out::println);
                System.out.println();
                System.out.println(mealController.getBetween(null, null, null, null));
            }

        } catch (Exception e) {
            log.error("database not initialized");
        }
    }
}
