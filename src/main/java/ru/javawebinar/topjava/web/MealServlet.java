package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImplInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDaoImplInMemory mealDaoImplInMemory = new MealDaoImplInMemory();

    public MealServlet(MealDaoImplInMemory mealDaoImplInMemory) {
        this.mealDaoImplInMemory = mealDaoImplInMemory;
    }

    public MealServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            log.debug("Get all");
            Collection<MealTo> mealToList = mealDaoImplInMemory.getAll();
            request.setAttribute("mealToList", mealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int mealId = getId(request);
            log.debug("Delete meal with id {}", mealId);
            mealDaoImplInMemory.delete(mealId);
            response.sendRedirect(request.getContextPath() + "/meals");
        } else {
            Meal meal = action.equals("create") ? new Meal(LocalDateTime.now(),
                    "some description", 500)
                    : mealDaoImplInMemory.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("edit.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String mealId = request.getParameter("id");
        return mealId != null ? Integer.parseInt(mealId) : 0;
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
        mealDaoImplInMemory.save(meal);
        log.debug("Meal save");
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
