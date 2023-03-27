package ru.job4j.accidents.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accidents.Job4jAccidentsApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс используется для выполнения модульных тестов
 * слоя контроллера аутентификации
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@AutoConfigureMockMvc
class LoginControllerTest {

    /**
     * Объект-заглушка
     */
    private MockMvc mockMvc;

    /**
     * Инициализация объекта-заглушки mockMvc до выполнения тестов
     */
    @BeforeEach
    public void whenSetUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new LoginController()).build();
    }

    /**
     * Тест проверяет сценарий отображения пользователю вида с моделью
     * errorMessage в случае, когда пользователь не прошёл аутентификацию в
     * приложении
     */
    @Test
    void whenCredentialsAreNotCorrectShouldReturnLoginPageWithErrorMessage() throws Exception {
        String errorMessage = "Username or Password is incorrect !!!";
        this.mockMvc.perform(get("/login?error=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attribute("errorMessage", errorMessage));
    }

    /**
     * Тест проверяет сценарий отображения пользователю вида с моделью
     * errorMessage в случае, когда пользователь выполнил logout в
     * приложении
     */
    @Test
    void whenLogoutIsSuccessfulShouldReturnLoginPageWithErrorMessage() throws Exception {
        String errorMessage = "You have been successfully logged out !!!";
        this.mockMvc.perform(get("/login?logout=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attribute("errorMessage", errorMessage));

    }

    /**
     * Тест проверяет сценарий выполнения редиректа на страницу
     * для аутентификации, когда пользователь выполнил logout в приложении
     */
    @Test
    void whenPerformLogoutShouldReturnRedirect() throws Exception {
        this.mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"));
    }
}