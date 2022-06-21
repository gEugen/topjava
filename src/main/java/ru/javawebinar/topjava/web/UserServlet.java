package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private UserRepository repository;
    private ClassPathXmlApplicationContext appCtx;
    private AdminRestController controller;
    private final boolean springContextEn = true;

    @Override
    public void init() {
        if (springContextEn) {
            log.debug("Spring context enable");
            appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
            controller = appCtx.getBean(AdminRestController.class);
            log.debug("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
        } else {
            log.debug("UserServlet repository direct connection enable");
            repository = new InMemoryUserRepository();
        }
    }

    @Override
    public void destroy() {
        log.debug("Spring context close");
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
//        List<User> users = repository.getAll();
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
