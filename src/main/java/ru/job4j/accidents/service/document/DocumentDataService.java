package ru.job4j.accidents.service.document;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Document;
import ru.job4j.accidents.repository.document.DocumentCrudRepository;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Сопроводительных документов
 */
@Service
@AllArgsConstructor
public class DocumentDataService implements DocumentService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * Сопроводительных документов
     */
    private final DocumentCrudRepository store;

    @Override
    public Document add(Document document) {
        store.save(document);
        return document;
    }

    @Override
    public boolean update(Document document) {
        store.save(document);
        return true;
    }

    @Override
    public Document delete(Document document) {
        store.delete(document);
        return document;
    }

    @Override
    public List<Document> findAll() {
        return (List<Document>) store.findAll();
    }

    @Override
    public Document findById(int id) {
        return store.findById(id)
                .orElseThrow(
                () -> new NoSuchElementException(
                        String.format("Document with id = %d not found in store", id)
                ));
    }

    @Override
    public List<Document> findAllByAccidentId(int accidentId) {
        return store.findAllByAccidentId(accidentId);
    }

    @Override
    public void saveAccidentDocumentsFromRequest(MultipartFile[] multipartFiles,
                                                 Accident accident, String author) {
        for (MultipartFile mpf
                : multipartFiles) {
            if (!mpf.isEmpty()) {
                Document document = new Document();
                try {
                    document.setData(mpf.getBytes());
                    document.setFileName(mpf.getOriginalFilename());
                    document.setAuthor(author);
                    document.setAccident(accident);
                    store.save(document);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void deleteAllByAccidentId(int accidentId) {
        store.deleteAllByAccidentId(accidentId);
    }

    @Override
    public void deleteAllByStatusId(int statusId) {
        store.deleteAllByStatusId(statusId);
    }
}
