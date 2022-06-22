package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private ConfigurableApplicationContext appCtx;
    private MealRestController controller;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, meal.getId());
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String submit = request.getParameter("submit");
        String startDateString = request.getParameter("startDate");
        String endDateString = request.getParameter("endDate");
        String startTimeString = request.getParameter("startTime");
        String endTimeString = request.getParameter("endTime");
        LocalDate startDate;
        LocalDate endDate;
        LocalTime startTime;
        LocalTime endTime;
        if (startDateString == null || "".equals(startDateString)) {
            startDate = LocalDate.MIN;
        } else {
            startDate = LocalDate.parse(startDateString);
        }
        if (endDateString == null || "".equals(endDateString)) {
            endDate = LocalDate.MAX;
        } else {
            endDate = LocalDate.parse(endDateString);
        }
        if (startTimeString == null || "".equals(startTimeString)) {
            startTime = LocalTime.MIN;
        } else {
            startTime = LocalTime.parse(startTimeString);
        }
        if (endTimeString == null || "".equals(endTimeString)) {
            endTime = LocalTime.MAX;
        } else {
            endTime = LocalTime.parse(endTimeString);
        }

        if ("submit".equals(submit)) {
            request.setAttribute("meals", controller.getFiltered(startDate, endDate, startTime, endTime));
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("startTime", startTime);
            request.setAttribute("endTime", endTime);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            switch (action == null ? "all" : action) {
                case "delete":
                    int id = getId(request);
                    log.info("Delete id={}", id);
                    controller.delete(id);
                    response.sendRedirect("meals");
                    break;
                case "create":
                case "update":
                    final Meal newMeal = "create".equals(action) ?
                            new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) : controller.get(getId(request));
                    request.setAttribute("meal", newMeal);
                    request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                    break;
                case "all":
                default:
                    log.info("getAll");
                    request.setAttribute("startDate", startDate);
                    request.setAttribute("endDate", endDate);
                    request.setAttribute("startTime", startTime);
                    request.setAttribute("endTime", endTime);
                    request.setAttribute("meals", controller.getAll());
                    request.getRequestDispatcher("/meals.jsp").forward(request, response);
                    break;
            }
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
