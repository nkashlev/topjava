package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealDao {

    List<MealTo> getAll();

    MealTo getById(Integer id);

    void create(Meal meal);

    void update(Meal meal);

    void delete(Integer id);

}
