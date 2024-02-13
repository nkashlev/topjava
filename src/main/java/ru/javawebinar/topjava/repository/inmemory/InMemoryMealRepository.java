package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repositoryUsersMeals = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final int USER_ID = 1;

    {
        MealsUtil.meals.forEach(meal -> save(meal, USER_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repositoryUsersMeals.computeIfAbsent(userId, u_id -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repositoryUsersMeals.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repositoryUsersMeals.get(userId);
        return mealMap != null ? mealMap.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Map<Integer, Meal> mealMap = repositoryUsersMeals.get(userId);
        return CollectionUtils.isEmpty(mealMap) ? Collections.emptyList() : mealMap.values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        Map<Integer, Meal> mealMap = repositoryUsersMeals.get(userId);
        return CollectionUtils.isEmpty(mealMap) ? Collections.emptyList() : mealMap.values()
                .stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}

