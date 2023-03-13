package ru.job4j.accidents.service.accident;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.Service;

import java.util.List;

/**
 * Сервис Автомобильных инцидентов
 */
public interface AccidentService extends Service<Accident> {

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
     * Удаляет все архивные автоинциденты
     */
    void deleteAllArchived();

    /**
     * Проверяет соответствие текущего статуса автоинцидента с тем,
     * который ожидается
     * @param accidentId идентификатор автоинцидента
     * @param statusId идентификатор статуса
     * @return объект Accident, если соотвествие найдено, иначе - null
     */
    Accident checkAccidentForStatus(int accidentId, int statusId);
}
