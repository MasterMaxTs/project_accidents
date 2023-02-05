package ru.job4j.accidents.repository;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище сущностей
 * @param <T> тип сущности
 */
public interface Repository<T> {

    T add(T model);

    boolean update(T model);

    T delete(T model);

    List<T> findAll();

    Optional<T> findById(int id);
}
