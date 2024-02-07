package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private InMemoryMealDao inMemoryMealDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        inMemoryMealDao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            log.debug("Get all");
            Collection<MealTo> mealToList = MealsUtil.filteredByStreams(
                    inMemoryMealDao.getAll(),
                    LocalTime.MIN, LocalTime.MAX,
                    2000);
            request.setAttribute("mealToList", mealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int mealId = getId(request);
            log.debug("Delete meal with id {}", mealId);
            inMemoryMealDao.delete(mealId);
            response.sendRedirect(request.getContextPath() + "/meals");
        } else if (action.equals("create")) {
            Meal meal = new Meal(LocalDateTime.now(), "some description", 500);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("edit.jsp").forward(request, response);
        } else if (action.equals("update")) {
            Meal meal = inMemoryMealDao.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("edit.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/meals");
        }
    }

    private int getId(HttpServletRequest request) throws IllegalArgumentException {
        String mealId = request.getParameter("id");
        if (mealId != null) {
            return Integer.parseInt(mealId);
        } else {
             throw new IllegalArgumentException("Non-existent id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String mealId = request.getParameter("id");
        String dateTimeString = request.getParameter("dateTime");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        Meal meal = new Meal(mealId.isEmpty() ? null : Integer.parseInt(mealId),
                dateTime,
                description,
                calories);
        inMemoryMealDao.save(meal);
        log.debug("Meal save");
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
