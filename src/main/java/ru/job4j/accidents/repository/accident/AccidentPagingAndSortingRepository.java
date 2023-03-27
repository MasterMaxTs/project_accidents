package ru.job4j.accidents.repository.accident;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.model.Accident;

import java.util.List;

/**
 * Доступ к хранилищу автомобильных инцидентов c помощью Spring Data
 */
public interface AccidentPagingAndSortingRepository extends
                             PagingAndSortingRepository<Accident, Integer> {

    /**
     * Возвращает список автоинцидентов пользователя
     * @param userName имя пользователя
     * @return список автоинцидентов пользователя
     */
    @Transactional
    @Query("from Accident a where a.user.username = :username")
    List<Accident> findAllByUser(@Param("username") String userName);

    /**
     * Возвращает список автоинцидентов из хранилища по statusId
     * @return список автоинцидентов по заданному статусу
     */
    @Transactional
    @Query("from Accident a where a.status.id = :stId")
    List<Accident> findAllByStatus(@Param("stId") int statusId);

    /**
     * Удаляет автоинцидент из хранилища по id
     * @param id идентификатор автоинцидента
     */
    @Transactional
    @Modifying
    void deleteAccidentById(int id);

    /**
     * Удаляет все автоинциденты из хранилища по statusId
     * @param statusId идентификатор статуса сопровождения автоинцидента
     */
    @Transactional
    @Modifying
    @Query("delete from Accident a where a.status.id = :stId")
    void deleteAllByStatus(@Param("stId") int statusId);
}