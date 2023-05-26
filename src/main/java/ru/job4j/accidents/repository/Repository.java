package ru.job4j.accidents.repository;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище сущностей
 * @param <T> тип сущности
 */
public interface Repository<T> {

    /**
     * Выполняет сохранение сущности в базу данных.
     * При успешном сохранении возвращает сущность,
     * у которой проинициализирован id.
     * @param model сохраняемая сущность в виде модели данных
     * @return сущность с проинициализированным id
     */
    T add(T model);

    /**
     * Выполняет обновление сущности в базе данных
     * @param model обновляемая сущность в виде модели данных
     * @return Boolean
     */
    boolean update(T model);

    /**
     * Выполняет удаление сущности из базы данных
     * @param model удаляемая сущность в виде модели данных
     * @return удалённую сущность
     */
    T delete(T model);

    /**
     * Возвращает параметризированный список всех
     * сущностей, находящихся в базе данных
     * @return параметризированный список всех сущностей
     */
    List<T> findAll();

    /**
     * Находит сущность в базе данных по id
     * @param id идентификатор сущности в виде модели данных
     * @return Optional.of(model), если сущность найдена, иначе Optional.empty()
     */
    Optional<T> findById(int id);
}
