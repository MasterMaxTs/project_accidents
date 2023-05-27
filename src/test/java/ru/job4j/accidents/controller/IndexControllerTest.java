package ru.job4j.accidents.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.job4j.accidents.Job4jAccidentsApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс используется для выполнения модульных тестов
 * контроллера начальной страницы
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@ActiveProfiles(value = "test")
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
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(new IndexController()).build();
    }

    /**
     * Тест проверяет сценарий сопоставления запрошенной главной страницы index
     * и редиректа на форму аутентификации
     */
    @Test
    void whenRequestIndexShouldReturnLoginPage() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}