package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.RegistrationCard;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.authority.AuthorityService;
import ru.job4j.accidents.service.card.RegistrationCardService;
import ru.job4j.accidents.service.user.UserService;

import java.time.LocalDateTime;

/**
 * Контроллер регистрации пользователей
 */
@Controller
@AllArgsConstructor
@Slf4j
public class RegController {

    /**
     * Ссылки на слои сервисов
     */
    private final UserService userService;
    private final AuthorityService authorityService;
    private final RegistrationCardService registrationCardService;
    private final PasswordEncoder encoder;

    /**
     * Возращает вид-форму для регистрации нового пользователя
     * @param user пользователь
     * @return вид по имени "user/registration/registration" с моделью User
     */
    @GetMapping("/register")
    public ModelAndView viewRegPage(@ModelAttribute User user) {
        return new ModelAndView("user/registration/registration");
    }

    /**
     * Производит регистрацию нового пользователя,
     * возвращает сообщение пользователю в зависимости от результата
     * процедуры регистрации
     * @param user пользователь - владелец автомобилей
     * @param card учётная карточка автомобиля пользователя
     * @param carPlates массив регистрационных знаков автомобилей пользователя
     * @param certificateNumbers массив номеров свидетельств регистраций
     * автомобилей пользователя
     * @param vinCodes массив значений VIN-кодов автомобилей пользователя
     * @return вид по имени "registration/registration-user-success" с
     * моделью User, если процедура регистрации прошла успешно,
     * иначе вид по имени user/registration/registration
     */
    @PostMapping("/register")
    public ModelAndView saveUser(@ModelAttribute User user,
                                 @ModelAttribute RegistrationCard card,
                                 @RequestParam("car.plate") String[] carPlates,
                                 @RequestParam("certificate.number") String[] certificateNumbers,
                                 @RequestParam("vin.code") String[] vinCodes) {
        try {
            user.setEnabled(true);
            user.setAuthority(authorityService.findByAuthority("ROLE_USER"));
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRegistrationCards(
                registrationCardService.collectRegistrationCardsFromRequest(user,
                                                                        card,
                                                                        carPlates,
                                                                        certificateNumbers,
                                                                        vinCodes)
            );
            user.setCreated(LocalDateTime.now());
            userService.add(user);
            log.info("New user(username={}, id={}) has been registered successfully!",
                            user.getUsername(), user.getId());
        } catch (RuntimeException re) {
            String errorMessage =
                    "Введены некорректные регистрационные данные!"
                    + " Проверте правильность заполнения карточки(ек) учёта"
                    + " автомобиля(ей). Возможно, названия email либо"
                    + " username уже используются другим пользователем приложения!";
            log.error("New user registration failed: {}", re.getMessage());
            user.setPassword("");
            card.setCarPlate(carPlates[0]);
            card.setCertificateNumber(certificateNumbers[0]);
            card.setVinCode(vinCodes[0]);
            return new ModelAndView("user/registration/registration")
                    .addObject("errorMessage", errorMessage)
                    .addObject("user", user)
                    .addObject("card", card);
        }
        return new ModelAndView("user/registration/registration-user-success")
                .addObject("user", user);
    }
}
