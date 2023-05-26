package ru.job4j.accidents.service.document;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Document;
import ru.job4j.accidents.service.Service;

import java.util.List;

/**
 * Сервис сопроводительных документов
 */
public interface DocumentService extends Service<Document> {

    /**
     * Возвращает список всех сопроводительных документов по Id автоинцидента
     * @param accidentId идентификатор автоинцидента
     * @return список всех сопроводительных документов
     */
    List<Document> findAllByAccidentId(int accidentId);

    /**
     * Сохраняет сопроводительные документы к автоинциденту из запроса в БД
     * @param multipartFiles MultipartFile
     * @param accident автоинцидент
     * @param author инициатор отправленных документов
     */
    void saveDocumentsFromRequest(MultipartFile[] multipartFiles,
                                  Accident accident, String author);

    /**
     * Удаляет все сопроводительные документы по Id автоинцидента
     * @param accidentId идентификатор автоинцидента
     */
    void deleteAllByAccidentId(int accidentId);

    /**
     * Удаляет все сопроводительные документы по статусу
     * сопровождения автоинцидента
     * @param statusId статус автоинцидента
     */
    void deleteAllByStatusId(int statusId);
}
