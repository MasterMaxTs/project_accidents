package ru.job4j.accidents.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accidents.Job4jAccidentsApplication;
import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.authority.AuthorityService;
import ru.job4j.accidents.service.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс используется для выполнения модульных тестов
 * контроллера пользователей
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class UserControllerTest {

    /**
     * Объект-заглушка
     */
    private MockMvc mockMvc;

    /**
     * Сервис заглушек
     */
    @MockBean
    private UserService userService;

    @MockBean
    private AuthorityService authorityService;

    /**
     * Ссылка на PasswordEncoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Argument captor
     */
    @Captor
    private ArgumentCaptor<User> captor;


    /**
     * Инициализация объекта-заглушки mockMvc до выполнения тестов
     */
    @BeforeEach
    void whenSetUp() {
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(
                                    new UserController(
                                            userService,
                                            authorityService,
                                            passwordEncoder)
                               ).build();
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при нахождении
     * всех пользователей, зарегистрированных в приложении
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindAllUsersShouldReturnPageWithData() throws Exception {
        List<User> users = List.of();
        doReturn(users).when(userService).findAll();
        this.mockMvc.perform(get("/getAllUsers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/all-users"))
                .andExpect(model().attribute("users", users));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * просмотра регистрационных данных пользователя в приложении
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenShowUserCredentialsShouldReturnPageWithData() throws Exception {
        User user = new User();
        user.setUsername("username");
        doReturn(user).when(userService).findByUserName(user.getUsername());
        this.mockMvc.perform(
                    get("/user/credentials")
                            .param("username", user.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile/show-user-credentials"))
                .andExpect(model().attribute("user", user));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * редактирования регистрационных данных пользователя в приложении
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenUpdateUserCredentialsShouldReturnPageForUpdateWithData() throws Exception {
        User user = new User();
        user.setId(1);
        doReturn(user).when(userService).findById(user.getId());
        this.mockMvc.perform(
                        get(String.format("/users/%d/edit", user.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile/edit-user-credentials"))
                .andExpect(model().attribute("user", user));
    }

    /**
     * Тест проверяет сценарий успешного обновления регистрационных
     * данных пользователя в приложении
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenSuccessfulUpdateUserCredentialsShouldReturnPageForInformAndVerifyPerformMethods()
                                                             throws Exception {
        int authorityId = 1;
        doReturn(new Authority("ROLE_USER")).when(authorityService).findById(authorityId);
        this.mockMvc.perform(
                        post("/updateUser")
                                .param("password", "newPassword")
                                .param("email", "email@test.ru")
                                .param("authority.id", String.valueOf(authorityId))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile/edit-user-credentials-success"));
        verify(authorityService).findById(authorityId);
        verify(userService).update(captor.capture());
        User value = captor.getValue();
        assertTrue(passwordEncoder.matches("newPassword", value.getPassword()));
        assertThat(value.getEmail()).isEqualTo("email@test.ru");
    }

    /**
     * Тест проверяет сценарий неуспешного обновления регистрационных
     * данных пользователя в приложении
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenUnSuccessfulUpdateUserCredentialsShouldReturnErrorPageWithData()
                                                            throws Exception {
        int authorityId = 1;
        User user = new User();
        doThrow(RuntimeException.class).when(userService).update(user);
        this.mockMvc.perform(
                post("/updateUser")
                        .param("password", "******")
                        .param("email", "email@test.ru")
                        .param("authority.id", String.valueOf(authorityId))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile/edit-user-credentials-prohibition"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("user", user));
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при попытке
     * изменить статус доступа пользователя в приложение
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenChangeUserAccessToAppShouldReturnPageForConfirm() throws Exception {
        User user = new User();
        user.setId(1);
        doReturn(user).when(userService).findById(user.getId());
        this.mockMvc.perform(
                        get(String.format("/users/%d/change_access/confirm", user.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/change-access-confirm"))
                .andExpect(model().attribute("user", user));
    }

    /**
     * Тест проверяет сценарий изменения доступа пользователю в приложение
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenChangeUserAccessToAppShouldReturnPageWithData() throws Exception {
        User user = new User();
        user.setId(1);
        user.setEnabled(true);
        doReturn(user).when(userService).findById(user.getId());
        this.mockMvc.perform(
                        post(String.format("/users/%d/change_access", user.getId()))
                                .param("enabled", String.valueOf(user.isEnabled()))
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("admin/customer/change-access-success"))
                    .andExpect(model().attributeExists("message"));
        verify(userService).findById(user.getId());
        verify(userService).update(captor.capture());
        assertFalse(captor.getValue().isEnabled());
    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при
     * успешном нахождении пользователя по username
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindUserByUsernameAndSuccessfulSearchShouldReturnPageWithData()
                                                            throws Exception {
        User user = new User();
        user.setUsername("username");
        doReturn(user).when(userService).findByUserName(user.getUsername());
        this.mockMvc.perform(
                    get("/getUserByUsername")
                            .param("username", user.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/show-user-data"))
                .andExpect(model().attribute("user", user));

    }

    /**
     * Тест проверяет сценарий сопоставления вида и модели при
     * неуспешном нахождении пользователя по username
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    void whenFindUserByUsernameAndUnsuccessfulSearchShouldReturnPageForInform()
                                                            throws Exception {
        doReturn(null).when(userService).findByUserName("username");
        this.mockMvc.perform(
                        get("/getUserByUsername")
                                .param("username", "username"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer/no-filtered-user-inform"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}