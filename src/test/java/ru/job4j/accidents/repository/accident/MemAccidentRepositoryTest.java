package ru.job4j.accidents.repository.accident;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.accidents.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс используется для выполнения модульных тестов при работе
 * с локальным хранилищем автомобильных инцидентов
 */
class MemAccidentRepositoryTest {

    /**
     * Хранилище автомобильных инцидентов
     */
    private AccidentRepository store;

    /**
     * Список пользователей приложения
     */
    private List<User> users;

    /**
     * Список автомобильных инцидентов пользователей приложения
     */
    private List<Accident> accidents;

    /**
     * Инициализация локального хранилища автомобильных автоинцидентов
     * начальными данными до выполнения тестов:
     * добавление четырёх автоинцидентов от двух пользователей приложения
     */
    @BeforeEach
    void whenSetUpThenAddFourAccidentsIntoMemStore() {
        store = new MemAccidentRepository();
        users = List.of(
                new User("username1", "password1", "test1@test.com"),
                new User("username2", "password2", "test2@test.com")
        );
        LocalDateTime create = LocalDateTime.now();
        Accident accident1 = new Accident(
                "x001xx123",
                "name1",
                new AccidentType(1, "Две машины"),
                "desc1",
                users.get(0),
                List.of(),
                "address1",
                new Status(TrackingStates.ACCEPTED_STATUS.getId(), "Принят")
        );
        accident1.setCreated(create);
        Accident accident2 = new Accident(
                "x001xx123",
                "name2",
                new AccidentType(3, "Машина и велосипед"),
                "desc2",
                users.get(1),
                List.of(),
                "address2",
                new Status(TrackingStates.QUEUED_STATUS.getId(), "Ожидание")
        );
        accident2.setCreated(create.minusHours(6L));
        Accident accident3 = new Accident(
                "x002xx123",
                "name3",
                new AccidentType(2, "Машина и человек"),
                "desc3",
                users.get(1),
                List.of(),
                "address3",
                new Status(TrackingStates.ARCHIVED_STATUS.getId(), "Архив")
        );
        accident3.setCreated(create.minusDays(2L));
        accident3.setUpdated(create.minusDays(1L));
        Accident accident4 = new Accident(
                "x003xx123",
                "name4",
                new AccidentType(2, "Машина и человек"),
                "desc4",
                users.get(1),
                List.of(),
                "address1",
                new Status(TrackingStates.ARCHIVED_STATUS.getId(), "Архив")
        );
        accident4.setCreated(create.minusDays(20L));
        accident4.setUpdated(create.minusDays(10L));
        accidents = List.of(accident1, accident2, accident3, accident4);
        accidents.forEach(a -> store.add(a));
    }

    /**
     * Тест проверяет сценарий корректного добавления нового
     * автоинцидента в локальное хранилище и правильное извлечение из него
     */
    @Test
    void whenAddNewAccidentThenMemStoreHasSameAccident() {
        Accident expected = new Accident(
                 "x005xx123",
                        "name5",
                        new AccidentType(2, "Машина и человек"),
                        "desc5",
                        users.get(0),
                        List.of(),
                        "address5",
                        new Status(TrackingStates.ACCEPTED_STATUS.getId(), "Принят")
                );
        store.add(expected);
        Optional<Accident> rsl = store.findById(5);
        assertTrue(rsl.isPresent());
        assertThat(rsl.get()).isEqualToComparingFieldByField(expected);
    }

    /**
     * Тест проверяет сценарий корректного обновления информации об
     * автоинциденте в локальном хранилище
     */
    @Test
    void whenUpdateAccidentThenMemStoreHasSameAccident() {
        Optional<Accident> accidentOptional = store.findById(1);
        assertTrue(accidentOptional.isPresent());
        Accident updatedAccident = accidentOptional.get();
        updatedAccident.setRules(List.of(new Rule(1, "Статья.1")));
        updatedAccident.setResolution("resolution");
        store.update(updatedAccident);
        Optional<Accident> rsl = store.findById(updatedAccident.getId());
        assertTrue(rsl.isPresent());
        assertEquals(List.of(new Rule(1, "Статья.1")), rsl.get().getRules());
        assertEquals("resolution", rsl.get().getResolution());
    }

    /**
     * Тест проверяет сценарий корректного удаления автоинцидента
     * из локального хранилища
     */
    @Test
    void whenDeleteAccidentThenMemStoreHasNotSameAccident() {
        Optional<Accident> accidentOptional = store.findById(1);
        assertTrue(accidentOptional.isPresent());
        store.delete(accidentOptional.get());
        assertTrue(store.findById(1).isEmpty());
    }

    /**
     * Тест проверяет сценарий нахождения всех автомобильных автоинцидентов,
     * находящихся в хранилище
     */
    @Test
    void whenFindAllAccidentsThenGetThemFromMemStore() {
        List<Accident> rsl = store.findAll();
        assertThat(rsl.size()).isEqualTo(4);
        assertEquals("name1", rsl.get(0).getName());
        assertEquals("name2", rsl.get(1).getName());
        assertEquals("name3", rsl.get(2).getName());
        assertEquals("name4", rsl.get(3).getName());
    }

    /**
     * Тест проверяет сценарий корректного нахождения автоинцидента
     * по его идентификатору в хранилище
     */
    @Test
    void whenFindAccidentByIdThenEitherGetItFromMemStoreOrGetOptionalEmpty() {
        Accident expected = accidents.get(0);
        expected.setId(1);
        Optional<Accident> rsl = store.findById(expected.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get()).isEqualToComparingFieldByField(expected);
        assertTrue(store.findById(5).isEmpty());
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов,
     * зарегистрированных определённым пользователем, в хранилище
     */
    @Test
    void whenFindAllAccidentsByUserNameThenGetThemFromMemStore() {
        String userName = users.get(1).getUsername();
        List<Accident> expected = List.of(
                accidents.get(1), accidents.get(2), accidents.get(3));
        List<Accident> rsl = store.findAllByUserName(userName);
        assertEquals(expected, rsl);
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов
     * со статусом 'Ожидание' в хранилище
     */
    @Test
    void whenFindAllQueuedAccidentsThenGetThemFromMemStore() {
        List<Accident> expected = List.of(accidents.get(1));
        List<Accident> rsl = store.findAllQueued();
        assertThat(rsl.size()).isEqualTo(1);
        assertEquals(rsl.get(0).getId(), expected.get(0).getId());
        assertEquals(rsl.get(0).getStatus().getId(), TrackingStates.QUEUED_STATUS.getId());
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов
     * со статусом 'Архив' в хранилище
     */
    @Test
    void whenFindAllArchivedAccidentsThenGetThemFromMemStore() {
        List<Accident> expected = List.of(
                accidents.get(2), accidents.get(3));
        List<Accident> rsl = store.findAllArchived();
        assertThat(rsl.size()).isEqualTo(2);
        assertEquals(rsl.get(0).getId(), expected.get(0).getId());
        assertEquals(rsl.get(1).getId(), expected.get(1).getId());
    }

    /**
     * Тест проверяет сценарий корректного удаления всех автоинцидентов из
     * хранилища, статус которых 'Архив'
     */
    @Test
    void whenDeleteAllArchivedAccidentsThenMemStoreHasNotSameAccidents() {
        store.deleteAllArchived();
        List<Accident> expected = List.of(
                accidents.get(0), accidents.get(1));
        List<Accident> stored = store.findAll();
        assertThat(stored.size()).isEqualTo(2);
        assertThat(stored.get(0).getId()).isEqualTo(expected.get(0).getId());
        assertThat(stored.get(1).getId()).isEqualTo(expected.get(1).getId());
    }

    /**
     * Тест проверяет сценарий нахождения списка отфильтрованных по типу
     *  и статусу сопровождения автоинцидентов в хранилище
     */
    @Test
    void whenFindAllAccidentsByTypeAndStatusThenGetThemFromMemStore() {
        List<Accident> expected = List.of(accidents.get(2), accidents.get(3));
        List<Accident> rsl = store.findAllByTypeAndStatus(
                                2, TrackingStates.ARCHIVED_STATUS.getId());
        assertThat(rsl.size()).isEqualTo(2);
        assertEquals(expected.get(0).getName(), rsl.get(0).getName());
        assertEquals(expected.get(1).getName(), rsl.get(1).getName());
    }

    /**
     * Тест проверяет сценарий нахождения списка отфильтрованных по адресу ДТП
     *  и периоду регистрации автоинцидентов в хранилище
     */
    @Test
    void whenFindAllAccidentsByAddressAndDateRangeThenGetThemFromMemStore() {
        List<Accident> expected = List.of(accidents.get(0), accidents.get(3));
        List<Accident> rsl = store.findAllByAddressAndDateRange(
                "address1", LocalDateTime.now().minusMonths(1L), LocalDateTime.now()
        );
        assertThat(rsl.size()).isEqualTo(2);
        assertEquals(expected.get(0).getName(), rsl.get(0).getName());
        assertEquals(expected.get(1).getName(), rsl.get(1).getName());
    }

    /**
     * Тест проверяет сценарий нахождения списка автоинцидентов,
     * зарегистрированных в приложении за минувшие сутки, в хранилище
     */
    @Test
    void whenFindAllAccidentsByRegisteredLastDayThenGetThemFromMemStore() {
        List<Accident> expected = List.of(accidents.get(0), accidents.get(1));
        List<Accident> rsl = store.findAllByRegisteredLastDay();
        assertThat(rsl.size()).isEqualTo(2);
        assertEquals(expected.get(0).getName(), rsl.get(0).getName());
        assertEquals(expected.get(1).getName(), rsl.get(1).getName());
    }

    /**
     * Тест проверяет сценарий поиска автоинцидентов
     * по регистрационному знаку автомобиля в хранилище
     */
    @Test
    void whenFindAllAccidentsByCarPlateThenGetThemFromMemStore() {
        String carPlate = "x001xx123";
        List<Accident> expected = List.of(accidents.get(0), accidents.get(1));
        List<Accident> rsl = store.findAllByCarPlate(carPlate);
        assertThat(rsl.size()).isEqualTo(2);
        assertEquals(expected.get(0).getName(), rsl.get(0).getName());
        assertEquals(expected.get(1).getName(), rsl.get(1).getName());
    }
}