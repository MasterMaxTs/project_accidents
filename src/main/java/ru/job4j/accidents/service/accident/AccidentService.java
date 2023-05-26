package ru.job4j.accidents.service.accident;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.Service;

import java.time.LocalDateTime;
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
     * Возвращает список автоинцидентов по регистрационному знаку автомобиля
     * @param carPlate регистрационный знак автомобиля
     * @return список автоинцидентов пользователя
     */
    List<Accident> findAllByCarPlate(String carPlate);

    /**
     * Возвращает список автоинцидентов, добавленных в очередь на рассмотрение
     * @return список автоинцидентов
     */
    List<Accident> findAllQueued();

    /**
     * Возвращает список автоинцидентов, возвращённых инициаторам,
     * для уточнения данных
     * @return список автоинцидентов
     */
    List<Accident> findAllReturned();

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
     * Проверяет соответствие статуса переданного автоинцидента с текущим
     * статусом этого автоинцидента в БД
     * @param accident  автоинцидент
     * @return результат проверки в виде Boolean
     */
    boolean checkAccidentForStatus(Accident accident);

    /**
     * Возвращает список автоинцидентов, отфильтрованных по типу
     * и статусу сопровождения
     * @param typeId идентификатор типа
     * @param statusId идентификатор статуса сопровождения
     * @return список отфильтрованных автоинцидентов и отсортированных по
     * времени регистрации в обратном порядке
     */
    List<Accident> findAllByTypeAndStatus(int typeId, int statusId);

    /**
     * Возвращает список автоинцидентов, отфильтрованных по адресу ДТП и
     * локальному времени регистрации в диапазоне дат
     * @param address адрес ДТП
     * @param after начальное значение диапазона дат в виде локального времи
     * регистрации автоинцидента
     * @param before конечное значение диапазона дат в виде локального времи
     * регистрации автоинцидента
     * @return список отфильтрованных автоинцидентов и отсортированных по
     * времени регистрации в обратном порядке
     */
    List<Accident> findAllByAddressAndDateRange(String address,
                                                LocalDateTime after,
                                                LocalDateTime before);

    /**
     * Возвращает список автоинцидентов, зарегистрированных в системе за
     * минувшие сутки
     * @return список отфильтрованных автоинцидентов и отсортированных по
     * времени регистрации в обратном порядке
     */
    List<Accident> findAllByRegisteredLastDay();
}
