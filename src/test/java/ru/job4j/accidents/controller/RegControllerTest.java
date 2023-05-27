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
import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.model.RegistrationCard;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.authority.AuthorityService;
import ru.job4j.accidents.service.card.RegistrationCardService;
import ru.job4j.accidents.service.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс используется для выполнения модульных тестов
 * контроллера регистрации
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
     * Сервис заглушек
     */
    @MockBean
    private UserService userService;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private RegistrationCardService registrationCardService;

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
    public void whenSetUp() {
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(
                        new RegController(
                                userService,
                                authorityService,
                                registrationCardService,
                                passwordEncoder))
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
     * Тест проверяет сценарий регистрации нового пользователя, когда вводимые
     * регистрационные данные некорректны
     */
    @Test
    void whenNewUserRegistersWithIncorrectCredentialsShouldReturnViewRegPageWithErrorMessage() throws Exception {
        String ownerName = "Имя владельца";
        String ownerSurname = "Фамилия владельца";
        String[] carPlates = new String[] {"x001xx123", "x002xx123"};
        String[] certificateNumbers = new String[] {"00AB000001", "00AB000002"};
        String[] vinCodes = new String[] {"A1B2C3D4F5G6H7J8K", "A2B3C4D5F6G7H8J9K"};
        String username = "username";
        String password = "********";
        doThrow(new RuntimeException()).when(userService).add(any(User.class));
        this.mockMvc.perform(
                post("/register")
                        .param("owner.name", ownerName)
                        .param("owner.surname", ownerSurname)
                        .param("car.plate", carPlates)
                        .param("certificate.number", certificateNumbers)
                        .param("vin.code", vinCodes)
                        .param("username", username)
                        .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration/registration"))
                .andExpect(model().attributeExists("errorMessage"));
        verify(userService).add(captor.capture());
    }

    /**
     * Тест проверяет сценарий успешной регистрации нового пользователя
     */
    @Test
    void whenNewUserRegistersShouldReturnViewRegPageForInform() throws Exception {
        String ownerName = "Имя владельца";
        String ownerSurname = "Фамилия владельца";
        String[] carPlates = new String[] {"x001xx123", "x002xx123"};
        String[] certificateNumbers = new String[] {"00AB000001", "00AB000002"};
        String[] vinCodes = new String[] {"A1B2C3D4F5G6H7J8K", "A2B3C4D5F6G7H8J9K"};
        User user = new User("username", "********", "test@test.com");
        RegistrationCard card = new RegistrationCard();
        card.setOwnerName(ownerName);
        card.setOwnerName(ownerSurname);
        List<RegistrationCard> cards = List.of(new RegistrationCard(), new RegistrationCard());
        doReturn(new Authority("ROLE_USER")).when(authorityService).findByAuthority("ROLE_USER");
        doReturn(cards).when(registrationCardService)
                .collectRegistrationCardsFromRequest(user,
                                                 card,
                                                 carPlates,
                                                 certificateNumbers,
                                                 vinCodes);
        this.mockMvc.perform(
                        post("/register")
                                .param("owner.name", ownerName)
                                .param("owner.surname", ownerSurname)
                                .param("car.plate", carPlates)
                                .param("certificate.number", certificateNumbers)
                                .param("vin.code", vinCodes)
                                .param("username", user.getUsername())
                                .param("password", user.getPassword()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration/registration-user-success"))
                .andExpect(model().attribute("user", user));
        verify(userService).add(captor.capture());
        User value = captor.getValue();
        assertThat(value.getUsername()).isEqualTo(user.getUsername());
        assertTrue(passwordEncoder.matches(user.getPassword(), value.getPassword()));
    }
}