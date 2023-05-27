package ru.job4j.accidents.service.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.accidents.Job4jAccidentsApplication;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Document;
import ru.job4j.accidents.repository.document.DocumentCrudRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Класс используется для выполнения модульных тестов
 * слоя сервиса Сопроводительных документов
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@ActiveProfiles(value = "test")
class DocumentDataServiceTest {

    @MockBean
    private DocumentCrudRepository repository;
    private DocumentDataService service;

    /**
     * Объект-заглушка
     */
    private MockMultipartFile[] mockMultipartFiles;

    /**
     * Argument captor
     */
    @Captor
    private ArgumentCaptor<Document> captor;

    /**
     * Инициализация сервиса до выполнения тестов
     */
    @BeforeEach
    void whenSetUp() {
        service = new DocumentDataService(repository);
        mockMultipartFiles =
                new MockMultipartFile[] {
                        new MockMultipartFile(
                        "photo1",
                        "photo1.jpg",
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        "some data1".getBytes())
        };
    }

    /**
     * Тест проверяет сценарий корректного получения сопроводительного
     * документа из параметров GET-запроса
     */
    @Test
    void whenSaveDocumentsFromRequestThenGet() {
        String author = "userName";
        Accident accident = new Accident();
        accident.setId(1);
        service.saveDocumentsFromRequest(mockMultipartFiles, accident, author);
        verify(repository).save(captor.capture());
        Document value = captor.getValue();
        assertThat(value.getFileName()).isEqualTo("photo1.jpg");
        assertThat(value.getData()).isEqualTo("some data1".getBytes());
        assertThat(value.getAuthor()).isEqualTo("userName");
        assertThat(value.getAccident().getId()).isEqualTo(accident.getId());
    }
}