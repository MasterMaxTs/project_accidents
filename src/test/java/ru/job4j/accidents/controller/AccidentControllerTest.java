package ru.job4j.accidents.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accidents.Job4jAccidentsApplication;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Status;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.rule.RuleService;
import ru.job4j.accidents.service.status.StatusService;
import ru.job4j.accidents.service.type.AccidentTypeService;
import ru.job4j.accidents.service.user.UserService;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс используется для выполнения модульных тестов
 * слоя контроллера автоинцидентов
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
                                userService))
                        .build();
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
        doReturn(List.of()).when(typeService).findAll();
        this.mockMvc.perform(get("/createAccident"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/accident/create-accident"))
                .andExpect(model().attribute("types", typeService.findAll()));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке изменить
     * автоинцидент, когда его текущий идентификатор статуса отличен от 1,
     * авторизовавшимся в приложении пользователем в роли USER
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void whenUpdateAccidentByUserAndStatusAccidentChangedByAdminShouldReturnErrorPage() throws Exception {
        int accidentId = 1;
        doReturn(null).when(accidentService).checkAccidentForStatus(accidentId, 1);
        this.mockMvc.perform(
                get(String.format("/accidents/%d/check_status", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/error/edit-accident-prohibition"))
                .andExpect(model().attribute("id", accidentId));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * изменить автоинцидент, авторизовавшимся в приложении пользователем в
     * роли USER
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void whenUpdateAccidentByUserAndStatusAccidentHasNotChangedByAdminShouldReturnPageForUpdate() throws Exception {
        int accidentId = 1;
        Accident accidentInDb = new Accident();
        doReturn(accidentInDb).when(accidentService).checkAccidentForStatus(accidentId, 1);
        doReturn(List.of()).when(typeService).findAll();
        this.mockMvc.perform(
                        get(String.format("/accidents/%d/check_status", accidentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/accident/edit-accident"))
                .andExpect(model().attribute("accident", accidentInDb))
                .andExpect(model().attribute("types", typeService.findAll()));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * добавления автоинцидента в очередь на рассмотрение,
     * авторизовавшимся в приложении пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void viewAddAccidentToQueueConfirm() throws Exception {
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
    void viewAddAccidentToArchiveConfirm() throws Exception {
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
    void viewDeleteAccidentConfirm() throws Exception {
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
    void viewDeleteAllArchivedConfirm() throws Exception {
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
    void viewUpdateAccidentByAdmin() throws Exception {
        int accidentId = 1;
        int statusId = 2;
        Accident accidentInDb = new Accident();
        Status statusInDb = new Status();
        doReturn(accidentInDb).when(accidentService).findById(accidentId);
        doReturn(statusInDb).when(statusService).findById(statusId);
        doReturn(List.of()).when(typeService).findAll();
        doReturn(List.of()).when(ruleService).findAll();
        this.mockMvc.perform(
                    get(String.format("/accidents/%d/edit", accidentId))
                        .param("status.id", String.valueOf(statusId))
                ).andDo(print())
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
    void show() throws Exception {
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
     * Тест проверяет сценарий сопоставления вида и модели при успешной попытке
     * удаления автоинцидента из архива, авторизовавшимся в приложении
     * пользователем в роли ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void delete() throws Exception {
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
    void deleteAllArchived() throws Exception {
        this.mockMvc.perform(get("/deleteAllArchived"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accident/delete-all-from-archive-success"));
    }
}