package ru.job4j.accidents.repository.document;

import ru.job4j.accidents.model.Document;
import ru.job4j.accidents.repository.Repository;

import java.util.List;

/**
 * Хранилище Сопроводительных документов
 */
public interface DocumentRepository extends Repository<Document> {

    /**
     * Находит список сопроводительных документов у автоинцидента по его id
     * @param accidentId идентификатор автоинцидента
     * @return список сопроводительных документов
     */
    List<Document> findAllByAccidentId(int accidentId);

    /**
     * Удаляет все сопороводительные документы у автоинцидента по его id
     * @param accidentId идентификатор автоинцидента
     */
    void deleteAllByAccidentId(int accidentId);

    /**
     * Удаляет все сопороводительные документы у автоинцидента по его статус id
     * @param statusId идентификатор статуса сопровождения автоинцидента
     */
    void deleteAllByStatusId(int statusId);
}
