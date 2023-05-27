package ru.job4j.accidents.repository.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.accidents.model.*;
import ru.job4j.accidents.repository.accident.AccidentRepository;
import ru.job4j.accidents.repository.accident.MemAccidentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс используется для выполнения модульных тестов при работе
 * с локальным хранилищем сопроводительных документов
 */
class MemDocumentRepositoryTest {

    /**
     * Хранилище сопроводительных документов
     */
    private DocumentRepository documentStore;

    /**
     * Хранилище автомобильных инцидентов
     */
    private AccidentRepository accidentStore;

    /**
     * Список автоинцидентов
     */
    private List<Accident> accidents;

    /**
     * Список сопроводительных документов
     */
    private List<Document> documents;

    /**
     * Инициализация локальных хранилищ автоинцидентов и
     * сопроводительных документов начальными данными до выполнения тестов:
     * добавление двух автоинцидентов и трёх сопроводительных документов
     */
    @BeforeEach
    void whenSetUpThenAddTwoDocumentsToAccidentIntoMemStore() {
        documentStore = new MemDocumentRepository();
        accidentStore = new MemAccidentRepository();
        Accident accident1 = new Accident();
        accident1.setUser(new User("user1", "password1", "test1@test.com"));
        accident1.setStatus(new Status(
                TrackingStates.ACCEPTED_STATUS.getId(), "Принят"));
        Accident accident2 = new Accident();
        accident2.setUser(new User("user2", "password2", "test2@test.com"));
        accident2.setStatus(new Status(
                        TrackingStates.ARCHIVED_STATUS.getId(), "Архив"));
        accidents = List.of(accident1, accident2);
        accidents.forEach(a -> accidentStore.add(a));
        Document document1 = new Document();
        document1.setAccident(accidents.get(0));
        document1.setFileName("fileName1");
        Document document2 = new Document();
        document2.setAccident(accidents.get(0));
        document2.setFileName("fileName2");
        Document document3 = new Document();
        document3.setAccident(accidents.get(1));
        document3.setFileName("fileName3");
        documents = List.of(document1, document2, document3);
        documents.forEach(doc -> documentStore.add(doc));
    }

    /**
     * Тест проверяет сценарий корректного добавления нового
     * сопроводительного документа в локальное хранилище и
     * правильное извлечение из него
     */
    @Test
    void whenAddNewDocumentThenMemStoreHasSameDocument() {
        Document newDocument = new Document();
        newDocument.setAccident(accidents.get(0));
        newDocument.setFileName("documentFileName");
        documentStore.add(newDocument);
        Optional<Document> optionalDocument = documentStore.findById(4);
        assertTrue(optionalDocument.isPresent());
        assertThat(optionalDocument.get().getFileName()).isEqualTo(
                "documentFileName");
    }

    /**
     * Тест проверяет сценарий корректного обновления информации об
     * сопроводительном документе в локальном хранилище
     */
    @Test
    void whenUpdateDocumentThenMemStoreHasSameDocument() {
        Optional<Document> documentOptional = documentStore.findById(1);
        assertTrue(documentOptional.isPresent());
        Document updatedDocument = documentOptional.get();
        updatedDocument.setFileName("newFileName");
        updatedDocument.setData(new byte[] {-128, 0, 127});
        documentStore.update(updatedDocument);
        Optional<Document> rsl =
                documentStore.findById(updatedDocument.getId());
        assertTrue(rsl.isPresent());
        assertEquals("newFileName", rsl.get().getFileName());
        assertThat(rsl.get().getData().length).isEqualTo(3);
    }

    /**
     * Тест проверяет сценарий корректного удаления сопроводительного документа
     * из локального хранилища
     */
    @Test
    void whenDeleteDocumentThenMemStoreHasNotSameDocument() {
        Optional<Document> optionalDocument = documentStore.findById(1);
        assertTrue(optionalDocument.isPresent());
        documentStore.delete(optionalDocument.get());
        assertTrue(documentStore.findById(1).isEmpty());
    }

    /**
     * Тест проверяет сценарий нахождения всех сопроводительных документов,
     * находящихся в хранилище
     */
    @Test
    void whenFindAllDocumentsThenGetThemFromMemStore() {
        List<Document> expected = documents;
        List<Document> rsl = documentStore.findAll();
        assertThat(rsl.size()).isEqualTo(3);
        assertEquals(expected.get(0).getId(), rsl.get(0).getId());
        assertEquals(expected.get(1).getId(), rsl.get(1).getId());
        assertEquals(expected.get(2).getId(), rsl.get(2).getId());
    }

    /**
     * Тест проверяет сценарий корректного нахождения сопроводительного
     * документа по его идентификатору в хранилище
     */
    @Test
    void whenFindDocumentByIdThenEitherGetItFromMemStoreOrGetOptionalEmpty() {
        Optional<Document> documentOptional = documentStore.findById(1);
        assertTrue(documentOptional.isPresent());
        assertThat(documentOptional.get().getId()).isEqualTo(1);
        assertTrue(documentStore.findById(4).isEmpty());
    }

    /**
     * Тест проверяет сценарий нахождения списка сопроводительных документов,
     * закреплённых за определённым автоинцидентом, в хранилище
     */
    @Test
    void whenFindAllDocumentsByAccidentIdThenGetThemFromMemStore() {
        int accidentId = accidents.get(0).getId();
        List<Document> rsl = documentStore.findAllByAccidentId(accidentId);
        assertThat(rsl.size()).isEqualTo(2);
        assertThat(rsl.get(0).getId()).isEqualTo(documents.get(0).getId());
        assertThat(rsl.get(1).getId()).isEqualTo(documents.get(1).getId());
    }

    /**
     * Тест проверяет сценарий удаления всех сопроводительных документов
     * из локального хранилища у конкретного автоинцидента
     */
    @Test
    void whenDeleteAllDocumentsByAccidentIdThenGetEmptyListFromMemStore() {
        int accidentId = accidents.get(0).getId();
        documentStore.deleteAllByAccidentId(accidentId);
        assertThat(documentStore.findAllByAccidentId(accidentId)).isEqualTo(List.of());
    }

    /**
     * Тест проверяет сценарий удаления всех сопроводительных документов
     * из локального хранилища, закреплённых за автоинцидентами, имеющими
     * определённый статус сопровождения
     */
    @Test
    void whenDeleteAllDocumentsByStatusIdThenMemStoreHasNotSameDocuments() {
        int accidentId = accidents.get(1).getId();
        int statusId = TrackingStates.ARCHIVED_STATUS.getId();
        documentStore.deleteAllByStatusId(statusId);
        assertThat(documentStore.findAllByAccidentId(accidentId)).isEqualTo(List.of());
    }
}