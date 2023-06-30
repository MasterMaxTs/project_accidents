package ru.job4j.accidents.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accidents.Job4jAccidentsApplication;
import ru.job4j.accidents.model.*;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.card.RegistrationCardService;
import ru.job4j.accidents.service.document.DocumentService;
import ru.job4j.accidents.service.rule.RuleService;
import ru.job4j.accidents.service.status.StatusService;
import ru.job4j.accidents.service.type.AccidentTypeService;
import ru.job4j.accidents.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс используется для выполнения модульных тестов
 * контроллера автоинцидентов
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class AccidentControllerTest {

    /**
     * Объект-заглушка
     */
    private MockMvc mockMvc;

    /**
     * Объект-заглушка
     */
    private MockMultipartFile mockMultipartFile;

    /**
     * Сервис заглушек
     */
    @MockBean
    @Qualifier("accidentDataService")
    private AccidentService accidentService;

    @MockBean
    @Qualifier("accidentTypeDataService")
    private AccidentTypeService typeService;

    @MockBean
    @Qualifier("ruleDataService")
    private RuleService ruleService;

    @MockBean
    @Qualifier("statusDataService")
    private StatusService statusService;

    @MockBean
    private UserService userService;

    @MockBean
    private RegistrationCardService registrationCardService;

    @MockBean
    private User user;

    @MockBean
    @Qualifier("documentDataService")
    private DocumentService documentService;

    /**
     * Argument captor
     */
    @Captor
    private ArgumentCaptor<Accident> captor;

    /**
     * Инициализация объекта-заглушки mockMvc до выполнения тестов
     */
    @BeforeEach
    void whenSetUp() {
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(
                        new AccidentController(
                                accidentService,
                                typeService,
                                ruleService,
                                statusService,
                                userService,
                                registrationCardService,
                                documentService))
                        .build();
        mockMultipartFile = new MockMultipartFile(
                "photo",
                "photo.jpg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "some data".getBytes()
        );
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при нахождении
     * всех автоинцидентов авторизовавшимся в приложении пользователем в роли
     * ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindAllAccidentsShouldReturnIndexPageWithData() throws Exception {
        doReturn(List.of()).when(accidentService).findAll();
        this.mockMvc.perform(get("/getAllAccidents"))
                .andDo(print())
                .andExpect(view().name("admin/index/index"))
                .andExpect(
                        model().attribute("accidents", accidentService.findAll())
                );
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при нахождении
     * всех автоинцидентов авторизовавшимся в приложении пользователем в роли
     * USER
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void whenFindAllAccidentsByUserShouldReturnIndexPageWithData() throws Exception {
        doReturn(List.of()).when(accidentService).findAllByUserName("user");
        this.mockMvc.perform(get("/getAllByUser"))
                .andDo(print())
                .andExpect(view().name("user/index/index"))
                .andExpect(
                        model().attribute("accidents", accidentService.findAllByUserName("user"))
                );
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при нахождении
     * всех автоинцидентов, добавленных в очередь на рассмотрение,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindAllQueuedAccidentsShouldReturnPageWithData() throws Exception {
        doReturn(List.of()).when(accidentService).findAllQueued();
        this.mockMvc.perform(get("/allQueued"))
                .andDo(print())
                .andExpect(view().name("admin/accident/all-queued-accidents"))
                .andExpect(
                        model().attribute("accidents", accidentService.findAllQueued())
                );
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при нахождении
     * всех автоинцидентов, добавленных в архив,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindAllArchivedAccidentsShouldReturnPageWithData() throws Exception {
        doReturn(List.of()).when(accidentService).findAllArchived();
        this.mockMvc.perform(get("/allArchived"))
                .andDo(print())
                .andExpect(view().name("admin/accident/all-archived-accidents"))
                .andExpect(
                        model().attribute("accidents", accidentService.findAllArchived())
                );
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при создании
     * нового автоинцидента, авторизовавшимся в приложении пользователем в
     * роли USER
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void whenCreateAccidentShouldReturnPageWithData() throws Exception {
        doReturn(user).when(userService).findByUserName("user");
        doReturn(List.of()).when(user).getRegistrationCards();
        doReturn(List.of()).when(typeService).findAll();
        this.mockMvc.perform(get("/createAccident"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/accident/create-accident"))
                .andExpect(model().attribute("registrationCards", user.getRegistrationCards()))
                .andExpect(model().attribute("types", typeService.findAll()));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * обычного пользователя изменить автоинцидент, когда по нему уже
     * проводятся работы со стороны администратора приложения
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void whenUpdateAccidentByUserAndStatusAccidentChangedByAdminShouldReturnErrorPage() throws Exception {
        int accidentId = 1;
        int statusId = 1;
        Accident accidentInDb = new Accident();
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        doReturn(new Status(statusId, "accepted"))
                .when(statusService).findById(statusId);
        doReturn(false)
                .when(accidentService).checkAccidentForStatus(accidentInDb);
        this.mockMvc.perform(
                        multipart("/updateAccident").file(mockMultipartFile)
                                .param("id", "1")
                                .param("name", "corrected name")
                                .param("type.id", "1")
                                .param("text", "corrected description")
                                .param("address", "accident address")
                                .param("status.id", String.valueOf(statusId))
                                .param("username", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/error/edit-accident-prohibition"))
                .andExpect(model().attribute("id", accidentId));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * добавления автоинцидента в очередь на рассмотрение,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenAddAccidentToQueueShouldReturnPageForConfirmWithData() throws Exception {
        int accidentId = 1;
        Accident accidentInDb = new Accident();
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        this.mockMvc.perform(
                        get(String.format("/accidents/%d/addToQueue/confirm", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/add-to-queue-confirm"))
                .andExpect(model().attribute("accident", accidentInDb));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * добавления автоинцидента в архив, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenAddAccidentToArchiveShouldReturnPageForConfirmWithData() throws Exception {
        int accidentId = 1;
        Accident accidentInDb = new Accident();
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        this.mockMvc.perform(
                        get(String.format("/accidents/%d/addToArchive/confirm", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/add-to-archive-confirm"))
                .andExpect(model().attribute("accident", accidentInDb));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * удаления автоинцидента из архива, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenDeleteAccidentShouldReturnPageForConfirmWithData() throws Exception {
        int accidentId = 1;
        Accident accidentInDb = new Accident();
        accidentInDb.setId(accidentId);
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        this.mockMvc.perform(
                        get(String.format("/accidents/%d/delete/confirm", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/delete-from-archive-confirm"))
                .andExpect(model().attribute("accident", accidentInDb));
    }

    /**
     * Тест проверяет сценарий сопоставления вида при попытке
     * удаления всех автоинцидентов из архива, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenDeleteAllAccidentsShouldReturnPageForConfirm() throws Exception {
        this.mockMvc.perform(get("/deleteAllArchived/confirm"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/delete-all-from-archive-confirm"));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * редактирования автоинцидента, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenUpdateAccidentByAdminShouldReturnPageForUpdateWithData() throws Exception {
        int accidentId = 1;
        Accident accidentInDb = new Accident();
        Status statusInDb = new Status();
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        doReturn(statusInDb).when(statusService).findById(TrackingStates.IN_WORKED_STATUS.getId());
        doReturn(List.of()).when(typeService).findAll();
        doReturn(List.of()).when(ruleService).findAll();
        this.mockMvc.perform(
                    get(String.format("/accidents/%d/edit", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/edit-accident"))
                .andExpect(model().attribute("accident", accidentInDb))
                .andExpect(model().attribute("types", typeService.findAll()))
                .andExpect(model().attribute("rules", ruleService.findAll()));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * просмотра автоинцидента, авторизовавшимся в приложении
     * пользователем в роли USER
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void whenShowAccidentShouldReturnPageWithData() throws Exception {
        int accidentId = 1;
        Accident accidentInDb = new Accident();
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        this.mockMvc.perform(
                        get(String.format("/accidents/%d", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/accident/show-accident"))
                .andExpect(model().attribute("accident", accidentInDb));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * просмотра карточки учёта автомобиля, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenShowRegistrationCardShouldReturnPageWithData() throws Exception {
        String carPlate = "x001xx123";
        RegistrationCard card = new RegistrationCard();
        doReturn(card)
                .when(registrationCardService).findByCarPlate(carPlate);
        this.mockMvc.perform(
                get("/registration-card")
                        .param("car_plate", carPlate)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/show-reg-card"))
                .andExpect(model().attribute("card", card));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при успешной попытке
     * удаления автоинцидента из архива, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenAccidentIsSuccessfullyDeletedShouldReturnPageForInformWithData() throws Exception {
        int accidentId = 1;
        Accident accidentInDb = new Accident();
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        this.mockMvc.perform(
                        get(String.format("/accidents/%d/delete", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/delete-from-archive-success"))
                .andExpect(model().attribute("accident", accidentInDb));
    }

    /**
     * Тест проверяет сценарий сопоставления вида при успешной попытке
     * удаления всех автоинцидентов из архива, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenAllAccidentsAreSuccessfullyDeletedShouldReturnPageForInform() throws Exception {
        this.mockMvc.perform(get("/deleteAllArchived"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/delete-all-from-archive-success"));
    }

    /**
     * Тест проверяет сценарий создания нового автоинцидента,
     * авторизовавшимся в приложении пользователем в роли USER
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void whenCreateAccidentShouldReturnPageForInformAndVerifyPerformMethods() throws Exception {
        int statusId = TrackingStates.ACCEPTED_STATUS.getId();
        doReturn(new AccidentType(1, "two cars"))
                .when(typeService).findById(1);
        doReturn(new User("user", "*****", "test@test.com"))
                .when(userService).findByUserName("user");
        doReturn(new Status(statusId, "accepted"))
                .when(statusService).findById(statusId);
        this.mockMvc.perform(
                    multipart("/saveAccident").file(mockMultipartFile)
                        .param("car.plate", "x005xx123")
                        .param("name", "new accident")
                        .param("type.id", "1")
                        .param("text", "description")
                        .param("address", "accident address")
                        .param("username", "user")
                        .param("photo", mockMultipartFile.getOriginalFilename())
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        view().name("user/accident/create-accident-success")
                );
        verify(typeService).findById(1);
        verify(userService).findByUserName("user");
        verify(statusService).findById(statusId);
        verify(accidentService).add(captor.capture());
        Accident value = captor.getValue();
        assertThat(value.getName()).isEqualTo("new accident");
        assertThat(value.getCarPlate()).isEqualTo("x005xx123");
        assertThat(value.getType().getName()).isEqualTo("two cars");
        assertThat(value.getStatus().getName()).isEqualTo("accepted");
        assertThat(value.getUser().getUsername()).isEqualTo("user");
    }

    /**
     * Тест проверяет сценарий обновления автоинцидента,
     * авторизовавшимся в приложении пользователем в роли USER
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_USER"})
    void whenUpdateAccidentByUserShouldReturnPageForInformAndVerifyPerformMethods() throws Exception {
        int statusId = TrackingStates.ACCEPTED_STATUS.getId();
        doReturn(new AccidentType(1, "two cars"))
                .when(typeService).findById(1);
        doReturn(new Status(statusId, "accepted"))
                .when(statusService).findById(statusId);
        doReturn(new User("user", "pass", "test@test.com"))
                .when(userService).findByUserName("user");
        this.mockMvc.perform(
                        multipart("/updateAccident").file(mockMultipartFile)
                                .param("id", "1")
                                .param("name", "corrected name")
                                .param("type.id", "1")
                                .param("text", "corrected description")
                                .param("address", "accident address")
                                .param("status.id", String.valueOf(statusId))
                                .param("username", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        view().name("admin/accident/edit-accident-success")
                );
        verify(typeService).findById(1);
        verify(statusService).findById(statusId);
        verify(userService).findByUserName("user");
        verify(accidentService).update(captor.capture());
        Accident value = captor.getValue();
        assertThat(value.getName()).isEqualTo("corrected name");
        assertThat(value.getText()).isEqualTo("corrected description");
    }

    /**
     * Тест проверяет сценарий обновления автоинцидента,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenUpdateAccidentByAdminShouldReturnPageForInformAndVerifyPerformMethods() throws Exception {
        int statusId = TrackingStates.RESOLVED_STATUS.getId();
        doReturn(new AccidentType(1, "two cars"))
                .when(typeService).findById(1);
        doReturn(List.of(
                    new Rule(1, "first rule"),
                    new Rule(3, "third rule"))
                ).when(ruleService).getRulesFromIds(new String[] {"1", "3"});
        doReturn(new Status(statusId, "resolved")
                ).when(statusService).findById(statusId);
        doReturn(new User("user", "pass", "test@test.com")
                ).when(userService).findByUserName("user");
        this.mockMvc.perform(
                        multipart("/updateAccident").file(mockMultipartFile)
                                .param("id", "1")
                                .param("name", "accident name")
                                .param("type.id", "1")
                                .param("text", "description")
                                .param("rIds", "1", "3")
                                .param("resolution", "resolution")
                                .param("address", "accident address")
                                .param("status.id", String.valueOf(statusId))
                                .param("username", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        view().name("admin/accident/edit-accident-success")
                );
        verify(typeService).findById(1);
        verify(statusService).findById(statusId);
        verify(userService).findByUserName("user");
        verify(accidentService).update(captor.capture());
        Accident value = captor.getValue();
        assertThat(value.getName()).isEqualTo("accident name");
        assertThat(value.getResolution()).isEqualTo("resolution");
        assertThat(value.getStatus().getName()).isEqualTo("resolved");
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * вызвать фильтрацию автоинцидентов из навигационного меню,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenGetFiltersShouldReturnPageForUpdateWithData() throws Exception {
        this.mockMvc.perform(get("/getFilters"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/all-filtered-accidents"));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при
     * фильтрации автоинцидентов, зарегистрированных
     * в приложении за минувшие сутки,авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindAllAccidentsByRegisteredLastDayShouldReturnPageWithData() throws Exception {
        List<Accident> filtered = List.of();
        doReturn(filtered).when(accidentService).findAllByRegisteredLastDay();
        this.mockMvc.perform(get("/accidents/filter/by_last_day"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/all-filtered-accidents"))
                .andExpect(model().attribute("accidents", filtered));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при
     * фильтрации автоинцидентов адресу ДТП и периоду регистрации в приложении,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindAllAccidentsByAddressAndDateRangeShouldReturnPageWithData() throws Exception {
        String address = "address";
        LocalDateTime create = LocalDateTime.now();
        LocalDateTime start = create.minusDays(2L);
        LocalDateTime end = create.minusDays(1L);
        List<Accident> filtered = List.of();
        doReturn(filtered).when(accidentService).findAllByAddressAndDateRange(address,
                                                                              start,
                                                                              end);
        this.mockMvc.perform(
                get("/accidents/filter/by_address_and_date")
                        .param("address", address)
                        .param("start", String.valueOf(start))
                        .param("end", String.valueOf(end))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/all-filtered-accidents"))
                .andExpect(model().attribute("accidents", filtered));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при
     * фильтрации автоинцидентов по типу и статусу сопровождения,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindAllAccidentsByTypeAndStatusShouldReturnPageWithData() throws Exception {
        int typeId = 1;
        int statusId = 2;
        List<Accident> filtered = List.of();
        doReturn(new AccidentType(typeId, "Принят")).when(typeService).findById(typeId);
        doReturn(new Status(statusId, "Ожидание")).when(statusService).findById(statusId);
        doReturn(filtered).when(accidentService).findAllByTypeAndStatus(typeId, statusId);
        this.mockMvc.perform(
                get("/accidents/filter/by_type_and_status")
                        .param("type.id", String.valueOf(typeId))
                        .param("status.id", String.valueOf(statusId))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/all-filtered-accidents"))
                .andExpect(model().attribute("accidents", filtered));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при
     * успешном поиске автоинцидентов по регистрационному знаку автомобиля из
     * навигационного меню приложения,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenPerformSuccessfulSearchByCarPlateShouldReturnPageWithData() throws Exception {
        String carPlate = "x001xx123";
        List<Accident> searched = List.of(new Accident());
        doReturn(searched).when(accidentService).findAllByCarPlate(carPlate);
        this.mockMvc.perform(
                get("/getAllByCarPlate")
                        .param("car.plate", carPlate)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/all-filtered-accidents"))
                .andExpect(model().attribute("accidents", searched));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели,
     * когда по регистрационному знаку автомобиля, введённому в строке поиска
     * навигационного меню, автоинцидентов не нашлось.
     * Роль пользователя ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenPerformUnsuccessfulSearchByCarPlateShouldReturnPageForInform() throws Exception {
        String carPlate = "x001xx123";
        List<Accident> searched = List.of();
        doReturn(searched).when(accidentService).findAllByCarPlate(carPlate);
        this.mockMvc.perform(
                        get("/getAllByCarPlate")
                                .param("car.plate", carPlate)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/no-filtered-accidents-inform"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}