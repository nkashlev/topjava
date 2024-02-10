package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements MealDao {

    private final AtomicInteger mealId = new AtomicInteger();

    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Collection<Meal> getAll() {
        return mealsMap.values();
    }

    @Override
    public Meal get(int id) {
        return mealsMap.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(mealId.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }
}
