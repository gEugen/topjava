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

        String forward="";
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        if (action.equalsIgnoreCase("save")){
            int id = Integer.parseInt(req.getParameter("id"));
            String dateString = req.getParameter("date");
            String description = req.getParameter("description");
//            new CrudMemoryServiceImp().saveMeal(id, new Meal());
            forward = LIST_MEAL_TO;
        }

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
            int id = Integer.parseInt(req.getParameter("id"));
            new CrudMemoryServiceImp().deleteMeal(id);
            forward = LIST_MEAL_TO;
            req.setAttribute("mealsTo", new CrudMemoryServiceImp().getMeals(lowerLimit, upperLimit, caloriesPerDay));
        } else if (action.equalsIgnoreCase("update")){
            forward = ADD_OR_UPDATE;
            int id = Integer.parseInt(req.getParameter("id"));
            Meal meal = new CrudMemoryServiceImp().getMeal(id);
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


}
