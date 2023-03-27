package ru.job4j.accidents.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accidents.Job4jAccidentsApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Класс используется для выполнения модульных тестов
 * слоя контроллера начальной страницы
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@AutoConfigureMockMvc
class IndexControllerTest {

    /**
     * Объект-заглушка
     */
    private MockMvc mockMvc;

    /**
     * Инициализация объекта-заглушки mockMvc до выполнения тестов
     */
    @BeforeEach
    void whenSetUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new IndexController()).build();
    }

    /**
     * Тест проверяет сценарий корректного перенаправления на адрес
     * начальной страницы для авторизовавшегося в приложении пользователя
     * с ролью USER
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void shouldReturnCorrectRedirectForAuthorityUser() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/getAllByUser"));
    }

    /**
     * Тест проверяет сценарий корректного перенаправления на адрес
     * начальной страницы для авторизовавшегося в приложении пользователя
     * с ролью ADMIN
     */
    @Test
    @WithMockUser(username = "root", authorities = {"ROLE_ADMIN"})
    public void shouldReturnCorrectRedirectForAuthorityAdmin() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/getAllAccidents"));
    }
}