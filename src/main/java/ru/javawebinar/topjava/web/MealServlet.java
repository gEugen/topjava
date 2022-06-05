package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.CrudMemoryServiceImp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String ADD_OR_UPDATE = "/meal.jsp";
    private static final String LIST_MEAL_TO = "/meals.jsp";
    LocalTime lowerLimit = LocalTime.of(0, 0);
    LocalTime upperLimit = LocalTime.of(23, 59);
    int caloriesPerDay = 2000;
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meal");

        req.setCharacterEncoding("UTF-8");


        String forward;
//        String action = req.getParameter("action");
//        if (action == null) {
//            action = "";
//        }

        LocalDateTime dateTime = getDateTime(req.getParameter("date"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        if (!dateTime.equals(LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0))) {
            new CrudMemoryServiceImp().saveMeal(dateTime, description, calories);
        }

        forward = LIST_MEAL_TO;
        req.setAttribute("mealsTo", new CrudMemoryServiceImp().getMeals(lowerLimit, upperLimit, caloriesPerDay));
        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");

//        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
//        resp.sendRedirect("meals.jsp");

        String forward;
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        if (action.equalsIgnoreCase("delete")){
            LocalDateTime dateTime = getDateTime(req.getParameter("date"));
            new CrudMemoryServiceImp().deleteMeal(dateTime);
            forward = LIST_MEAL_TO;
            req.setAttribute("mealsTo", new CrudMemoryServiceImp().getMeals(lowerLimit, upperLimit, caloriesPerDay));
        } else if (action.equalsIgnoreCase("update")){
            forward = ADD_OR_UPDATE;
            LocalDateTime dateTime = getDateTime(req.getParameter("date"));
            Meal meal = new CrudMemoryServiceImp().getMeal(dateTime);
            req.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("add")){
            forward = ADD_OR_UPDATE;
            req.setAttribute("meal", new CrudMemoryServiceImp().getDefaultMeal());
        } else {
            forward = LIST_MEAL_TO;
            req.setAttribute("mealsTo", new CrudMemoryServiceImp().getMeals(lowerLimit, upperLimit, caloriesPerDay));
        }

        RequestDispatcher view = req.getRequestDispatcher(forward);
//        req.setAttribute("mealsTo", mealToList);
        view.forward(req, resp);
    }

    private LocalDateTime getDateTime(String dateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dateTime);

    }



}
