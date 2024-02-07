package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements MealDao {

    private final AtomicInteger mealId = new AtomicInteger();

    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);

        for (int i = 0; i < 2; i++) {
            save(new Meal(dateTime, "Завтрак", 500));
            save(new Meal(dateTime.plusHours(3), "Обед", 1000));
            save(new Meal(dateTime.plusHours(6), "Ужин", 500));
            dateTime = dateTime.plusDays(1);
        }
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
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
        } else if (!mealsMap.containsKey(meal.getId())) {
            return null;
        }
        mealsMap.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }
}
