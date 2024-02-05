package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.Collection;

public interface MealDao {

    Collection<MealTo> getAll();

    Meal get(int id);

    Meal save(Meal meal);

    void delete(int id);

}
