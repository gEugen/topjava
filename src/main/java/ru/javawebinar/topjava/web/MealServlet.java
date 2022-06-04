package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.servise.CrudMemoryServiceImp;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");

        LocalTime lowerLimit = LocalTime.of(0, 0);
        LocalTime upperLimit = LocalTime.of(23, 59);
        int caloriesPerDay = 2000;
//        List<MealTo> mealToList = MealsUtil.filteredByStreams(new CrudMemoryServiceImp().getMeals(), lowerLimit, upperLimit, caloriesPerDay);
        List<MealTo> mealToList = new CrudMemoryServiceImp().getMeals(lowerLimit, upperLimit, caloriesPerDay);

//        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
//        resp.sendRedirect("meals.jsp");

        RequestDispatcher view = req.getRequestDispatcher("meals.jsp");
        req.setAttribute("mealsTo", mealToList);
        view.forward(req, resp);
    }


}
