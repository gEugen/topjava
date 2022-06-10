package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
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
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    static final LocalTime LOWER_LIMIT_TIME = LocalTime.MIN;
    static final LocalTime UPPER_LIMIT_TIME = LocalTime.MAX;
    static final int CALORIES_PER_DAY = 2000;
    static final String MEAL_JSP = "/meal.jsp";
    static final String MEALS_JSP = "/meals.jsp";
    static final String MEAL_SERVLET_URL = "meals";
    private static final Logger LOG = getLogger(MealServlet.class);
    private final MealCrudMemoryService crudService = new MealCrudMemoryServiceImp();
//    private final Meal defaultMealTo = new Meal(0, LocalDateTime.of(LocalDateTime.now().toLocalDate().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0), "", 0);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("redirects to meal");

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        if (action.equalsIgnoreCase("save")) {
            LOG.debug("chooses a doPost action branch");
            Integer mealId = Integer.parseInt(req.getParameter("id"));
            LocalDateTime dateTime = getDateTime(req.getParameter("date"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));

            switch (getAction(mealId)){
                case "add":
                    LOG.debug("switched to doPost add branch");
                    crudService.add(getFormMeal(mealId, dateTime, description, calories));
                    break;

                case "update":
                    LOG.debug("switched to doPost update branch");
                    crudService.update(getFormMeal(mealId, dateTime, description, calories));
                    break;
            }

        } else {
            LOG.debug("switched to cancel or no action doPost branch");
        }

        resp.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("redirects to meals");

        String forward = MEALS_JSP;
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "add":
                LOG.debug("switched to doGet add branch");
                forward = MEAL_JSP;
                req.setAttribute("meal", getDefaultMealTo());
                break;

            case "update":
                LOG.debug("switched to doGet update branch");
                String s = req.getParameter("id");
                int id = Integer.parseInt(req.getParameter("id"));
                Meal requestedMeal = crudService.getMeal(id);
                forward = MEAL_JSP;
                req.setAttribute("meal", requestedMeal);
                break;

            case "delete":
                LOG.debug("switched to doGet delete branch");
                id = Integer.parseInt(req.getParameter("id"));
                LocalDateTime dateTime = getDateTime(req.getParameter("date"));
                String description = req.getParameter("description");
                int calories = Integer.parseInt(req.getParameter("calories"));
                Meal deletedMeal = getFormMeal(id, dateTime, description, calories);
                crudService.deleteMeal(id, deletedMeal);
                resp.sendRedirect(MEAL_SERVLET_URL);
                return;

            default:
                LOG.debug("switched to doGet default branch");
                List<MealTo> mealToList = crudService.getMeals(LOWER_LIMIT_TIME, UPPER_LIMIT_TIME, CALORIES_PER_DAY);
                req.setAttribute("mealsTo", mealToList);
                break;
        }

        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    private Meal getDefaultMealTo() {
        return new Meal(0, LocalDateTime.of(LocalDateTime.now().toLocalDate().getYear(),
                LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(),
                0, 0), "", 0);
    }

    private LocalDateTime getDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime);
    }

    private Meal getFormMeal(Integer mealId, LocalDateTime dateTime, String description, int calories) {
        return new Meal(mealId, dateTime, description, calories);
    }

    private String getAction(Integer mealId) {
        return mealId == 0 ? "add" : "update";
    }
}
