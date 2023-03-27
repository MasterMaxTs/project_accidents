package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.authority.AuthorityService;
import ru.job4j.accidents.service.user.UserService;

/**
 * Контроллер регистрации пользователей
 */
@Controller
@AllArgsConstructor
public class RegController {

    /**
     * Ссылки на слои сервисов
     */
    private final UserService userService;
    private final AuthorityService authorityService;
    private final PasswordEncoder encoder;

    /**
     * Возращает вид-форму для регистрации нового пользователя
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
     * @param user User
     * @return вид по имени "registration/registration-user-success" с
     * моделью User, если процедура регистрации прошла успешно,
     * иначе вид по имени user/registration/registration
     */
    @PostMapping("/register")
    public ModelAndView regSave(@ModelAttribute User user) {
        try {
            user.setEnabled(true);
            user.setAuthority(authorityService.findByAuthority("ROLE_USER"));
            user.setPassword(encoder.encode(user.getPassword()));
            userService.add(user);
            System.out.printf("New user with id=%d has been registered "
                    + "successfully", user.getId());
        } catch (RuntimeException re) {
            String errorMessage = String.format(
                    "There is a user with name '%s' in the "
                        + "database, set a different name!!", user.getUsername()
            );
            return new ModelAndView("user/registration/registration")
                    .addObject("errorMessage", errorMessage);
        }
        return new ModelAndView("user/registration/registration-user-success");
    }
}
