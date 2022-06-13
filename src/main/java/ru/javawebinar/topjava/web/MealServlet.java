package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.crud.MealCrud;
import ru.javawebinar.topjava.crud.InMemoryMealCrud;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final int CALORIES_PER_DAY = 2000;
    private static final String MEAL_JSP = "/meal.jsp";
    private static final String MEALS_JSP = "/meals.jsp";
    private static final String SERVLET_URL = "meals";
    private static final Logger log = getLogger(MealServlet.class);
    private MealCrud crud;

    @Override
    public void init() throws ServletException {
        crud = new InMemoryMealCrud();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.debug("redirects to meal");
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("save".equals(action)) {
            log.debug("chooses doPost action branch");
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("date"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));

            try {
                int id = Integer.parseInt(req.getParameter("id"));
                log.debug("switched to doPost update branch");
                crud.update(new Meal(id, dateTime, description, calories));
            } catch (NumberFormatException e) {
                log.debug("switched to doPost add branch");
                crud.add(new Meal(null, dateTime, description, calories));
            }
        }

        resp.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.debug("redirects to meals");
        String forward = MEALS_JSP;
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "add":
                log.debug("switched to doGet add branch");
                forward = MEAL_JSP;
                req.setAttribute("headName", "Add meal");
                req.setAttribute("meal", getDefaultMealTo());
                break;

            case "update":
                log.debug("switched to doGet update branch");
                int id = Integer.parseInt(req.getParameter("id"));
                forward = MEAL_JSP;
                req.setAttribute("headName", "Edit meal");
                req.setAttribute("meal", crud.get(id));
                break;

            case "delete":
                log.debug("switched to doGet delete branch");
                crud.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(SERVLET_URL);
                return;

            default:
                log.debug("switched to doGet default branch");
                req.setAttribute("mealsTo", MealsUtil.filteredByStreams(crud.getAll(),
                        LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
                break;
        }

        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    private Meal getDefaultMealTo() {
        return new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
    }
}
