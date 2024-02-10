package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.dao.MealDao;
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

    private static final int CALORIES_PER_DAY = 2000;

    private MealDao mealDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealDao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "all":
                log.debug("Get all");
                Collection<MealTo> mealToList = MealsUtil.filteredByStreams(
                        mealDao.getAll(),
                        LocalTime.MIN, LocalTime.MAX,
                        CALORIES_PER_DAY);
                request.setAttribute("mealToList", mealToList);
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                break;
            case "delete":
                int mealId = getId(request);
                log.debug("Delete meal with id {}", mealId);
                mealDao.delete(mealId);
                response.sendRedirect(request.getContextPath() + "/meals");
                break;
            case "create":
                Meal meal = new Meal(LocalDateTime.now(), "some description", 500);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("editMeal.jsp").forward(request, response);
                break;
            case "update":
                request.setAttribute("meal", mealDao.get(getId(request)));
                request.getRequestDispatcher("editMeal.jsp").forward(request, response);
                break;
            default:
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
        Meal meal = new Meal(mealId.isEmpty() ? null : Integer.parseInt(mealId),
                LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        mealDao.save(meal);
        log.debug("Meal save");
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
