package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfNextDayOrMax;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal get(int id, int userId) {
        return ValidationUtil.checkNotFoundWithId(repository.get(id, userId), id);
    }

    public void delete(int id, int userId) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
    }

    public void create(Meal meal, int userId) {
        ValidationUtil.checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public void update(Meal meal, int userId) {
        ValidationUtil.checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}