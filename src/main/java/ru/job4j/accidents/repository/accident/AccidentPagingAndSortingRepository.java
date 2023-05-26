package ru.job4j.accidents.repository.accident;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.job4j.accidents.model.Accident;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Доступ к хранилищу автомобильных инцидентов c помощью Spring Data
 */
public interface AccidentPagingAndSortingRepository extends
                             PagingAndSortingRepository<Accident, Integer> {

    /**
     * Возвращает список автоинцидентов пользователя
     * @param userName имя пользователя
     * @return список автоинцидентов пользователя, отсортированных по
     * статусу сопровождения в естественном порядке и времени регистрации в
     * обратном порядке
     */
    @SuppressWarnings("checkstyle:MethodName")
    List<Accident> findAllByUser_UsernameOrderByStatusAscCreatedDesc(String userName);


    /**
     * Возвращает список автоинцидентов по регистрационному знаку автомобиля
     * @param carPlate регистрационный знак автомобиля
     * @return список автоинцидентов, отсортированных по
     * статусу сопровождения в естественном порядке и времени регистрации в
     * обратном порядке
     */
    List<Accident> findAllByCarPlateOrderByStatusAscCreatedDesc(String carPlate);

    /**
     * Возвращает список автоинцидентов из хранилища по statusId
     * @param statusId идентификатор статуса сопровождения
     * @return список автоинцидентов по заданному статусу
     */
    @SuppressWarnings("checkstyle:MethodName")
    List<Accident> findAllByStatus_Id(int statusId);

    /**
     * Удаляет автоинцидент из хранилища по id
     * @param id идентификатор автоинцидента
     */
    @Modifying
    void deleteAccidentById(int id);

    /**
     * Удаляет все автоинциденты из хранилища по statusId
     * @param statusId идентификатор статуса сопровождения автоинцидента
     */
    @SuppressWarnings("checkstyle:MethodName")
    @Modifying
    void deleteAllByStatus_Id(int statusId);

    /**
     * Возвращает список автоинцидентов, отфильтрованных по типу
     * и статусу сопровождения
     * @param typeId идентификатор автоинцидента
     * @param statusId  идентификатор статуса сопровождения
     * @return список отфильтрованных автоинцидентов и отсортированных по
     * времени регистрации в обратном порядке
     */
    @SuppressWarnings("checkstyle:MethodName")
    List<Accident> findAllByType_IdAndStatus_IdOrderByCreatedDesc(int typeId,
                                                                  int statusId);

    /**
     * Возвращает список автоинцидентов, отфильтрованных по адресу ДТП и
     * локальному времени регистрации в диапазоне дат
     * @param address адрес ДТП
     * @param start начальное значение диапазона дат в виде локального времени
     * регистрации автоинцидента
     * @param end конечное значение диапазона дат в виде локального времени
     * регистрации автоинцидента
     * @return список отфильтрованных автоинцидентов и отсортированных по
     * времени регистрации в обратном порядке
     */
    List<Accident> findAllByAddressAndCreatedBetweenOrderByCreatedDesc(String address,
                                                                       LocalDateTime start,
                                                                       LocalDateTime end);

    /**
     * Возвращает список автоинцидентов, зарегистрированных в системе за
     * указанный период времени
     * @param start начальное время регистрации
     * @param end конечное время регистрации
     * @return список отфильтрованных автоинцидентов и отсортированных по
     * времени регистрации в обратном порядке
     */
    List<Accident> findAllByCreatedBetweenOrderByCreatedDesc(LocalDateTime start,
                                                             LocalDateTime end);
}