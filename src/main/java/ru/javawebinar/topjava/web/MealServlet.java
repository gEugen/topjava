package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.service.MealCrudMemoryService;
import ru.javawebinar.topjava.service.MealCrudMemoryServiceImp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String LIST_MEAL_TO = "/meals.jsp";
    LocalTime lowerLimit = LocalTime.of(0, 0);
    LocalTime upperLimit = LocalTime.of(23, 59);
    int caloriesPerDay = 2000;
    private static final Logger log = getLogger(MealServlet.class);

    // This is the Singleton of the CRUD Memory interface.
    private static final MealCrudMemoryService crudService = MealCrudMemoryServiceImp.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");

        req.setAttribute("mealsTo", crudService.getMeals(lowerLimit, upperLimit, caloriesPerDay));
        RequestDispatcher view = req.getRequestDispatcher(LIST_MEAL_TO);
        view.forward(req, resp);
    }
}
