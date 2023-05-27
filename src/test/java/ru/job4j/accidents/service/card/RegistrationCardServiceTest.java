package ru.job4j.accidents.service.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.accidents.Job4jAccidentsApplication;
import ru.job4j.accidents.model.RegistrationCard;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.card.RegistrationCardCrudRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс используется для выполнения модульных тестов
 * слоя сервиса Карточек учёта ТС
 */
@SpringBootTest(classes = Job4jAccidentsApplication.class)
@ActiveProfiles(value = "test")
class RegistrationCardServiceTest {

    @MockBean
    private RegistrationCardCrudRepository repository;
    private RegistrationCardDataService service;

    /**
     * Инициализация сервиса до выполнения тестов
     */
    @BeforeEach
    void whenSetUp() {
        service = new RegistrationCardDataService(repository);
    }

    /**
     * Тест проверяет сценарий корректного получения списка карточек учёта из
     * параметров GET-запроса
     */
    @Test
    void whenCollectRegistrationCardsFromRequestThenGet() {
        User user = new User();
        RegistrationCard card = new RegistrationCard();
        String ownerName = "ownerName";
        String ownerSurname = "ownerSurname";
        card.setOwnerName(ownerName);
        card.setOwnerSurname(ownerSurname);
        String[] carPlates = new String[] {"х001хх11", "х002хх11" };
        String[] certificateNumbers = new String[] {"12АВ345678", "13АВ987654"};
        String[] vinCodes = new String[] {"A1B2C3D4F5G6H7J8K", "A2B3C4D5F6G7H8J9K"};
        List<RegistrationCard> rsl =
                        service.collectRegistrationCardsFromRequest(user,
                                                                    card,
                                                                    carPlates,
                                                                    certificateNumbers,
                                                                    vinCodes);
        assertThat(rsl.size()).isEqualTo(2);
        assertThat(rsl.get(0).getCarPlate()).isEqualTo("х001хх11");
        assertThat(rsl.get(1).getCertificateNumber()).isEqualTo("13АВ987654");
    }
}