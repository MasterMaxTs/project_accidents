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

    private final UserService userService;
    private final AuthorityService authorityService;
    private final PasswordEncoder encoder;

    /**
     * Возращает вид для регистрации нового пользователя
     * @return вид по имени "registration/registration" с моделью User
     */
    @GetMapping("/register")
    public ModelAndView viewRegPage(@ModelAttribute User user) {
        return new ModelAndView("registration/registration");
    }

    /**
     * Производит регистрацию нового пользователя
     * @param user User
     * @return вид по имени "registration/registration-user-success" с моделью User
     */
    @PostMapping("/register")
    public ModelAndView regSave(@ModelAttribute User user) {
        String userName = user.getUsername();
        if (userService.existsByUsername(userName)) {
            String message = String.format(
                    "There is a user with name '%s' in the database,"
                            + " set a different name!", userName);
            return new ModelAndView("registration/registration")
                    .addObject("errorMessage", message);
        }
        user.setEnabled(true);
        user.setAuthority(authorityService.findByAuthority("ROLE_USER"));
        user.setPassword(encoder.encode(user.getPassword()));
        userService.add(user);
        System.out.printf("New user with id=%d has been registered "
                + "successfully", user.getId());
        return new ModelAndView("registration/registration-user-success");

    }
}
