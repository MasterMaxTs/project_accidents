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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accidents.Job4jAccidentsApplication;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.user.UserCrudRepository;
import ru.job4j.accidents.service.authority.AuthorityService;
import ru.job4j.accidents.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс используется для выполнения модульных тестов
 * слоя контроллера регистрации
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class RegControllerTest {

    /**
     * Объект-заглушка
     */
    private MockMvc mockMvc;

    /**
     * Ссылки на слои сервисов
     */
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserCrudRepository repository;

    /**
     * Argument captor
     */
    @Captor
    private ArgumentCaptor<User> captor;

    /**
     * Инициализация объекта-заглушки mockMvc до выполнения тестов
     */
    @BeforeEach
    public void whenSetUp() {
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(
                        new RegController(userService, authorityService, passwordEncoder))
                        .build();
    }

    /**
     * Тест проверяет сценарий корректного сопоставления вида
     * при регистрации нового пользователя
     */
    @Test
    void shouldReturnViewRegPage() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration/registration"));
    }

    /**
     * Тест проверяет сценарий регистрации нового пользователя, когда вводимое
     * имя пользователя не может быть использовано
     */
    @Test
    void whenNewUserRegistersWithIncorrectCredentialsShouldReturnViewRegPageWithErrorMessage() throws Exception {
        doThrow(new RuntimeException()).when(repository).save(any(User.class));
        this.mockMvc.perform(
                post("/register")
                        .param("username", "wrongName")
                        .param("password", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration/registration"))
                .andExpect(model().attributeExists("errorMessage"));
        verify(repository).save(captor.capture());
    }

    /**
     * Тест проверяет сценарий успешной регистрации нового пользователя
     */
    @Test
    void whenNewUserRegistersShouldReturnViewRegPageForInform() throws Exception {
        User newUser = new User("username", "pass");
        this.mockMvc.perform(
                        post("/register")
                                .param("username", newUser.getUsername())
                                .param("password", newUser.getPassword()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration/registration-user-success"));
        verify(repository).save(captor.capture());
        User value = captor.getValue();
        assertThat(value.getUsername()).isEqualTo(newUser.getUsername());
        assertTrue(passwordEncoder.matches(newUser.getPassword(), value.getPassword()));
    }
}