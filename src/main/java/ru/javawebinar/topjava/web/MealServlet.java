package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDaoImpl mealDaoImpl = new MealDaoImpl();

    public MealServlet(MealDaoImpl mealDaoImpl) {
        this.mealDaoImpl = mealDaoImpl;
    }

    public MealServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealToList = mealDaoImpl.getAll();
        request.setAttribute("mealToList", mealToList);
        log.debug("Redirecting a request to the JSP page meals.jsp to display a list of meal entries");
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
