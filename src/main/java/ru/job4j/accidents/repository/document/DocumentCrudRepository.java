package ru.job4j.accidents.repository.document;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.model.Document;

import java.util.List;

/**
 * Доступ к хранилищу Сопроводительных документов c помощью Spring Data
 */
public interface DocumentCrudRepository
                             extends CrudRepository<Document, Integer> {

    /**
     * Возвращает список всех сопроводительных документов по Id автоинцидента
     * @param accidentId идентификатор автоинцидента
     * @return список всех сопроводительных документов
     */
    @Transactional
    @Query("from Document d where d.accident.id = :acId ")
    List<Document> findAllByAccidentId(@Param("acId") int accidentId);

    /**
     * Удаляет все сопроводительные документы по Id автоинцидента
     * @param accidentId идентификатор автоинцидента
     */
    @Transactional
    @Modifying
    @Query("delete from Document d where d.accident.id = :acId")
    void deleteAllByAccidentId(@Param("acId") int accidentId);

    /**
     * Удаляет все сопроводительные документы по статусу
     * сопровождения автоинцидента
     * @param statusId статус автоинцидента
     */
    @Transactional
    @Modifying
    @Query("delete from Document d where d.accident.id IN "
            + "(select a.id from Accident a where a.status.id = :stId)")
    void deleteAllByStatusId(@Param("stId") int statusId);
}
