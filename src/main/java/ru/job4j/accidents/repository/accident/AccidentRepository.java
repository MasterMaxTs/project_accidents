package ru.job4j.accidents.repository.accident;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.Repository;

import java.util.List;

/**
 * Хранилище Автомобильных инцидентов
 */
public interface AccidentRepository extends Repository<Accident> {

    /**
     * Возвращает список автоинцидентов пользователя
     * @param userName имя пользователя
     * @return список автоинцидентов пользователя
     */
    List<Accident> findAllByUserName(String userName);

    /**
     * Возвращает список автоинцидентов, добавленных в очередь на рассмотрение
     * @return список автоинцидентов
     */
    List<Accident> findAllQueued();

    /**
     * Возвращает список автоинцидентов, добавленных в архив
     * @return список автоинцидентов
     */
    List<Accident> findAllArchived();

    /**
     * Удаляет все архивные автоинциденты (очистка архива)
     */
    void deleteAllArchived();
}
