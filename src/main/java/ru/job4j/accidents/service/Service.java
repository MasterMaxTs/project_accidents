package ru.job4j.accidents.service;

import java.util.List;

/**
 * Слой сервисов
 * @param <T> тип сущности
 */
public interface Service<T> {

    T add(T model);

    boolean update(T model);

    T delete(T model);

    List<T> findAll();

    T findById(int id);
}
