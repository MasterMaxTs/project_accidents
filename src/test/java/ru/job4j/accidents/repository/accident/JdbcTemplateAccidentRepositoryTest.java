package ru.job4j.accidents.repository.accident;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.accidents.Job4jAccidentsApplication;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.rule.RulePagingAndSortingRepository;
import ru.job4j.accidents.repository.status.StatusPagingAndSortingRepository;
import ru.job4j.accidents.repository.type.AccidentTypePagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс используется для выполнения интеграционных тестов при выявлении
 * правильного взаимодействия приложения с хранилищем автоинцидентов c
 * помощью JdbcTemplate
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@ActiveProfiles(value = "test")
class JdbcTemplateAccidentRepositoryTest {

    /**
     * Внедрение зависимости от jdbcTemplate
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Внедрение зависимости от хранилища автомобильных инцидентов
     */
    @Autowired
    private JdbcTemplateAccidentRepository accidentStore;

    /**
     * Внедрение зависимости от хранилища типов автомобильных инцидентов
     */
    @Autowired
    private AccidentTypePagingAndSortingRepository typeStore;

    /**
     * Внедрение зависимости от хранилища статусов сопровождения
     * автомобильных инцидентов
     */
    @Autowired
    private StatusPagingAndSortingRepository statusStore;

    /**
     * Внедрение зависимости от хранилища статей автонарушений
     */
    @Autowired
    private RulePagingAndSortingRepository ruleStore;

    /**
     * Текущий пользователь приложения
     */
    private User root;

    /**
     * Список автомобильных инцидентов пользователей приложения
     */
    private List<Accident> accidents;

    /**
     * Инициализация хранилища автомобильных автоинцидентов
     * начальными данными до выполнения тестов:
     * добавление четырёх автоинцидентов от пользователя приложения
     */
    @BeforeEach
    void whenSetUpThenAddFourAccidentsPerUserIntoDBStore() {
        root = new User("root", "*******", "test@test.com");
        root.setId(1);
        LocalDateTime create = LocalDateTime.now();
        Accident accident1 = new Accident(
                "x001xx123",
                "name1",
                typeStore.findById(1).get(),
                "desc1",
                root,
                List.of(),
                "address1",
                statusStore.findById(1).get()
        );
        accident1.setCreated(create);
        Accident accident2 = new Accident(
                "x001xx123",
                "name2",
                typeStore.findById(3).get(),
                "desc2",
                root,
                List.of(),
                "address2",
                statusStore.findById(3).get()
        );
        accident2.setCreated(create.minusDays(1L));
        Accident accident3 = new Accident(
                "x002xx123",
                "name3",
                typeStore.findById(2).get(),
                "desc3",
                root,
                List.of(),
                "address3",
                statusStore.findById(6).get()
        );
        accident3.setCreated(create.minusDays(2L));
        Accident accident4 = new Accident(
                "x003xx123",
                "name4",
                typeStore.findById(2).get(),
                "desc4",
                root,
                List.of(),
                "address1",
                statusStore.findById(6).get()
        );
        accident4.setCreated(create.minusDays(3L));
        accidents = List.of(accident1, accident2, accident3, accident4);
        accidents.forEach(accident -> accidentStore.add(accident));
    }

    /**
     * Очистка таблиц accidents_rules, accidents после выполнения
     * каждого теста
     */
    @AfterEach
    void wipeDBTablesAfterEachTest() {
        jdbcTemplate.update("delete from accidents_rules");
        jdbcTemplate.update("delete from accidents");
    }

    /**
     * Тест проверяет сценарий корректного добавления нового
     * автоинцидента в базу данных и правильное извлечение из неё
     */
    @Test
    void whenAddNewAccidentThenDBStoreHasSameAccident() {
        Accident expected = new Accident(
                "x005xx123",
                "name5",
                typeStore.findById(2).get(),
                "desc5",
                root,
                List.of(),
                "address5",
                statusStore.findById(1).get()
        );
        expected.setCreated(LocalDateTime.now());
        accidentStore.add(expected);
        Optional<Accident> rsl = accidentStore.findById(expected.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getName()).isEqualTo(expected.getName());
        assertThat(rsl.get().getType().getName()).isEqualTo("Машина и человек");
        assertThat(rsl.get().getStatus().getName()).isEqualTo("Принят");
    }

    /**
     * Тест проверяет сценарий корректного обновления информации об
     * автоинциденте в базе данных
     */
    @Test
    void whenUpdateAccidentThenDBStoreHasSameAccident() {
        Accident accidentInDB = accidentStore.findAll().get(0);
        accidentInDB.setRules(List.of(ruleStore.findById(1).get(),
                                      ruleStore.findById(3).get()));
        accidentInDB.setResolution("Текст постановления");
        accidentInDB.setStatus(statusStore.findById(4).get());
        accidentStore.update(accidentInDB);
        Optional<Accident> optionalAccident = accidentStore.findById(accidentInDB.getId());
        assertTrue(optionalAccident.isPresent());
        assertThat(optionalAccident.get().getRules().size()).isEqualTo(2);
        assertThat(optionalAccident.get().getResolution()).isEqualTo("Текст постановления");
        assertThat(optionalAccident.get().getStatus().getId()).isEqualTo(4);
    }

    /**
     * Тест проверяет сценарий корректного удаления автоинцидента
     * из базы данных
     */
    @Test
    void whenDeleteAccidentThenDBStoreHasNotSameAccident() {
        Accident accidentInDB = accidentStore.findAll().get(0);
        accidentInDB.setRules(List.of(ruleStore.findById(1).get()));
        accidentInDB.setResolution("Текст постановления");
        accidentStore.update(accidentInDB);
        accidentStore.delete(accidentInDB);
        assertThrows(EmptyResultDataAccessException.class,
                        () -> accidentStore.findById(accidentInDB.getId())
        );
        assertThat(
              jdbcTemplate.query(
                      "select * from accidents_rules where accident_id = ?",
                      (ResultSetExtractor<List<Integer>>) rs -> {
                          List<Integer> ruleIDs = new ArrayList<>();
                          while (rs.next()) {
                              ruleIDs.add(rs.getInt("rule_id"));
                          }
                          return ruleIDs;
                      },
                      accidentInDB.getId()
            )).isEqualTo(List.of());
    }

    /**
     * Тест проверяет сценарий нахождения всех отсортированных автомобильных
     * автоинцидентов, находящихся в базе данных
     */
    @Test
    void whenFindAllAccidentsThenGetThemFromDBStore() {
        List<Accident> accidentsFromDB = accidentStore.findAll();
        assertThat(accidentsFromDB.size()).isEqualTo(4);
        assertThat(accidentsFromDB.get(0).getStatus().getId()).isEqualTo(1);
        assertThat(accidentsFromDB.get(1).getStatus().getId()).isEqualTo(3);
        assertThat(accidentsFromDB.get(2).getStatus().getId()).isEqualTo(6);
        assertThat(accidentsFromDB.get(3).getStatus().getId()).isEqualTo(6);
    }

    /**
     * Тест проверяет сценарий нахождения автоинцидента
     * по его идентификатору в базе данных.
     * Если автоинцидент найден в базе данных по идентификатору, то возвращается
     * значение из Optional, иначе выбрасывается исключение
     * EmptyResultDataAccessException.
     */
    @Test()
    void whenFindAccidentByIdThenGetItFromDBStoreOrGetEmptyResultDataAccessException() {
        Accident accidentInDB = accidentStore.findAll().get(0);
        assertTrue(accidentStore.findById(accidentInDB.getId()).isPresent());
        assertThrows(EmptyResultDataAccessException.class,
                        () -> accidentStore.findById(1000)
        );
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов,
     * зарегистрированных определённым пользователем, в базе данных
     */
    @Test
    void whenFindAllAccidentsByUserNameThenGetThemFromDBStore() {
        List<Accident> accidentsInDB =
                            accidentStore.findAllByUserName(root.getUsername());
        assertThat(accidentsInDB.size()).isEqualTo(4);
        assertThat(accidentsInDB.get(0).getStatus().getId()).isEqualTo(1);
        assertThat(accidentsInDB.get(3).getStatus().getId()).isEqualTo(6);
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов
     * со статусом 'Ожидание' в базе данных
     */
    @Test
    void whenFindAllQueuedAccidentsThenGetThemFromDBStore() {
        List<Accident> expected = List.of(accidents.get(1));
        List<Accident> queuedAccidents = accidentStore.findAllQueued();
        assertThat(queuedAccidents.size()).isEqualTo(1);
        assertThat(queuedAccidents.get(0).getName()).isEqualTo(expected.get(0).getName());
        assertThat(queuedAccidents.get(0).getStatus()).isEqualTo(expected.get(0).getStatus());
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов
     * со статусом 'Архив' в базе данных
     */
    @Test
    void whenFindAllArchivedAccidentsThenGetThemFromDBStore() {
        List<Accident> expected = List.of(accidents.get(3), accidents.get(2));
        List<Accident> archivedAccidentsInDB = accidentStore.findAllArchived();
        assertThat(archivedAccidentsInDB.size()).isEqualTo(2);
        assertThat(archivedAccidentsInDB.get(0).getStatus()).isEqualTo(expected.get(0).getStatus());
        assertThat(archivedAccidentsInDB.get(1).getStatus()).isEqualTo(expected.get(1).getStatus());
    }

    /**
     * Тест проверяет сценарий корректного удаления всех автоинцидентов из
     * базы данных, статус которых 'Архив'
     */
    @Test
    void whenDeleteAllArchivedAccidentsThenDBStoreHasNotSameAccidents() {
        List<Accident> archivedAccidentsInDB = accidentStore.findAllArchived();
        assertThat(archivedAccidentsInDB.size()).isEqualTo(2);
        accidentStore.deleteAllArchived();
        assertThrows(EmptyResultDataAccessException.class,
                        () -> accidentStore.findById(archivedAccidentsInDB.get(0).getId())
        );
        assertThrows(EmptyResultDataAccessException.class,
                () -> accidentStore.findById(archivedAccidentsInDB.get(1).getId())
        );
        assertThat(accidentStore.findAll().size()).isEqualTo(2);
    }

    /**
     * Тест проверяет сценарий нахождения списка отфильтрованных по типу
     *  и статусу сопровождения автоинцидентов в базе данных
     */
    @Test
    void whenFindAllAccidentsByTypeAndStatusThenGetThemFromDBStore() {
        List<Accident> filtered = accidentStore.findAllByTypeAndStatus(2, 6);
        assertThat(filtered.size()).isEqualTo(2);
        assertThat(filtered.get(0).getType().getId()).isEqualTo(2);
        assertThat(filtered.get(1).getType().getId()).isEqualTo(2);
        assertThat(filtered.get(1).getStatus().getId()).isEqualTo(6);
        assertThat(filtered.get(1).getStatus().getId()).isEqualTo(6);
    }

    /**
     * Тест проверяет сценарий нахождения списка отфильтрованных по адресу ДТП
     *  и периоду регистрации автоинцидентов в базе данных
     */
    @Test
    void whenFindAllAccidentsByAddressAndDateRangeThenGetThemFromDBStore() {
        LocalDateTime current = LocalDateTime.now();
        String address = "address1";
        LocalDateTime start = current.minusDays(3L);
        LocalDateTime end = current.minusDays(1L);
        List<Accident> filtered =
                accidentStore.findAllByAddressAndDateRange(address, start, end);
        assertThat(filtered.size()).isEqualTo(1);
        assertThat(filtered.get(0).getName()).isEqualTo("name4");
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов,
     * зарегистрированных в приложении за минувшие сутки, в базе данных
     */
    @Test
    void whenFindAllAccidentsByRegisteredLastDayThenGetThemFromDBStore() {
        List<Accident> filtered = accidentStore.findAllByRegisteredLastDay();
        assertThat(filtered.size()).isEqualTo(2);
        assertThat(filtered.get(0).getName()).isEqualTo("name1");
        assertThat(filtered.get(1).getName()).isEqualTo("name2");
    }

    /**
     * Тест проверяет сценарий поиска автоинцидентов
     * по регистрационному знаку автомобиля в базе данных
     */
    @Test
    void whenFindAllAccidentsByCarPlateThenGetThemFromMemStore() {
        String carPlate = "x001xx123";
        List<Accident> expected = List.of(accidents.get(0), accidents.get(1));
        List<Accident> searched = accidentStore.findAllByCarPlate(carPlate);
        assertThat(searched.size()).isEqualTo(2);
        assertThat(searched.get(0).getName()).isEqualTo(expected.get(0).getName());
        assertThat(searched.get(1).getName()).isEqualTo(expected.get(1).getName());
    }
}