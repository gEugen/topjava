package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealCrudMemoryService;
import ru.javawebinar.topjava.service.MealCrudMemoryServiceImp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String ADD_OR_UPDATE = "/meal.jsp";
    private static final String LIST_MEAL_TO = "/meals.jsp";
    LocalTime lowerLimit = LocalTime.of(0, 0);
    LocalTime upperLimit = LocalTime.of(23, 59);
    int caloriesPerDay = 2000;
    private static final Logger log = getLogger(MealServlet.class);

    // This is the Singleton of the CRUD Memory interface.
    private static final MealCrudMemoryService crudService = MealCrudMemoryServiceImp.getInstance();

    // This is the accumulator of meal for its field values updating.
    private static Meal mealAccumulator;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meal");

        req.setCharacterEncoding("UTF-8");
        String forward;
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        if (action.equalsIgnoreCase("save")) {
            LocalDateTime dateTime = getDateTime(req.getParameter("date"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));
            crudService.saveMeal(mealAccumulator, dateTime, description, calories);
        }

        forward = LIST_MEAL_TO;
        req.setAttribute("mealsTo", crudService.getMeals(lowerLimit, upperLimit, caloriesPerDay));
        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");

        // ***** The code is left here for future analysis.
//        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
//        resp.sendRedirect("meals.jsp");

        String forward;
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        if (action.equalsIgnoreCase("delete")){
            LocalDateTime dateTime = getDateTime(req.getParameter("date"));
            crudService.deleteMeal(dateTime);

            // ***** The code is left here for future analysis.
//            forward = LIST_MEAL_TO;
//            req.setAttribute("mealsTo", crudService.getMeals(lowerLimit, upperLimit, caloriesPerDay));

            resp.sendRedirect("meals");
            return;

        } else if (action.equalsIgnoreCase("update")){
            LocalDateTime dateTime = getDateTime(req.getParameter("date"));
            mealAccumulator = crudService.getMeal(dateTime);
            if (mealAccumulator != null) {
                forward = ADD_OR_UPDATE;
                req.setAttribute("meal", mealAccumulator);

            } else {
                forward = LIST_MEAL_TO;
                req.setAttribute("mealsTo", crudService.getMeals(lowerLimit, upperLimit, caloriesPerDay));
            }

        } else if (action.equalsIgnoreCase("add")){
            mealAccumulator = null;
            forward = ADD_OR_UPDATE;
            req.setAttribute("meal", crudService.getDefaultMeal());

        } else {
            forward = LIST_MEAL_TO;
            req.setAttribute("mealsTo", crudService.getMeals(lowerLimit, upperLimit, caloriesPerDay));
        }

        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    private LocalDateTime getDateTime(String dateTime) {

        // ***** The code is left here for future analysis.
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return LocalDateTime.parse(dateTime);

    }
}
