package ru.job4j.accidents.repository.document;

import lombok.NoArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Document;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Реализация потокобезопасного локального хранилища Сопроводительных документов
 */
@Repository
@NoArgsConstructor
@ThreadSafe
public class MemDocumentRepository implements DocumentRepository {

    /**
     * Хранилище в виде СoncurrentHashMap
     */
    private final Map<Integer, List<Document>> documents =
                                            new ConcurrentHashMap<>();

    private final AtomicInteger atomicInteger = new AtomicInteger(1);


    @Override
    public Document add(Document document) {
        int accidentId = document.getAccident().getId();
        document.setId(atomicInteger.getAndIncrement());
        List<Document> documentList = new ArrayList<>();
        documents.compute(accidentId,
                            (key, value) -> {
                                if (value == null) {
                                    value = documentList;
                                }
                                value.add(document);
                                return value;

        });
        return document;
    }

    @Override
    public boolean update(Document document) {
        int accidentId = document.getAccident().getId();
        documents.computeIfPresent(
                accidentId,
                    (key, value) -> {
                        value.removeIf(d -> d.getId() == document.getId());
                        value.add(document);
                        return value;
                    }
        );
        return true;
    }

    @Override
    public Document delete(Document document) {
        int accidentId = document.getAccident().getId();
        documents.computeIfPresent(
                    accidentId,
                        (key, value) -> {
                            value.removeIf(d -> d.getId() == document.getId());
                            return value;
                        }
        );
        return document;
    }

    @Override
    public List<Document> findAll() {
        return documents.values()
                        .stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<Document> findById(int id) {
        return documents.values()
                        .stream()
                        .flatMap(Collection::stream)
                        .filter(a -> a.getId() == id)
                        .findFirst();
    }

    @Override
    public List<Document> findAllByAccidentId(int accidentId) {
        List<Document> documentsInMem = documents.get(accidentId);
        return documentsInMem == null ? Collections.emptyList()
                : new ArrayList<>(documentsInMem);
    }

    @Override
    public void deleteAllByAccidentId(int accidentId) {
        documents.remove(accidentId);
    }

    @Override
    public void deleteAllByStatusId(int statusId) {
        documents.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream())
                .filter(doc -> doc.getAccident().getStatus().getId() == statusId)
                .collect(Collectors.toList())
                .forEach(this::delete);
    }
}
