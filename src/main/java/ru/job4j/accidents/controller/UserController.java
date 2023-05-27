package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.authority.AuthorityService;
import ru.job4j.accidents.service.user.UserService;

import java.time.LocalDateTime;

/**
 * Контроллер пользователей приложения
 */
@Controller
@AllArgsConstructor
@Slf4j
public class UserController {

    /**
     * Ссылки на слои сервисов
     */
    private UserService userService;
    private AuthorityService authorityService;
    private PasswordEncoder encoder;

    /**
     * Добавляет имя пользователя, авторизовавшегося в приложении,
     * во все модели контроллера
     * @return имя авторизовавшегося в приложении пользователя
     */
    @ModelAttribute("username")
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Возращает вид с заполненным списком всех пользователей
     * @return вид по имени "admin/customer/all-users"
     * с заполненным списком всех пользователей
     */
    @GetMapping("/getAllUsers")
    public ModelAndView getAll() {
        return new ModelAndView("admin/customer/all-users")
                .addObject("users", userService.findAll());
    }

    /**
     * Возвращает вид с регистрационными данными пользователя
     * @param username username пользователя
     * @return возвращает вид по имени profile/show-user-credentials с
     * данными о пользователе
     */
    @GetMapping("/user/credentials")
    public ModelAndView viewUserCredentials(
                                @RequestParam("username") String username) {
        return new ModelAndView("profile/show-user-credentials")
                .addObject("user", userService.findByUserName(username));
    }

    /**
     * Возращает вид для редактирования регистрационных данных пользователя
     * @param userId идентификатор пользователя
     * @return вид по имени "profile/edit-user-credentials" в виде формы с
     * данными о пользователе
     */
    @GetMapping("/users/{userId}/edit")
    public ModelAndView viewUpdateUserCredentials(
                                @PathVariable("userId") int userId) {
        User user = userService.findById(userId);
        user.setPassword("");
        return new ModelAndView("profile/edit-user-credentials")
                    .addObject("user", user);
    }

    /**
     * Обновляет регистрационные данные пользователя в хранилище
     * @param user пользователь
     * @param authorityId идентификатор роли
     * @return вид по имени profile/edit-user-credentials-success с username
     * пользователя,если обновление данных прошло успешно, иначе вид по имени
     * profile/edit-user-credentials-prohibition с сообщением об ошибке и
     * данными о пользователе
     */
    @PostMapping("/updateUser")
    public ModelAndView update(@ModelAttribute User user,
                               @RequestParam("authority.id") int authorityId) {
        try {
            user.setAuthority(authorityService.findById(authorityId));
            user.setPassword(encoder.encode(user.getPassword()));
            user.setUpdated(LocalDateTime.now());
            userService.update(user);
        } catch (RuntimeException re) {
            String errorMessage =
                    "Введены некорректные регистрационные данные!"
                            + " Указанный email уже используется"
                            + " другим пользователем приложения!"
                            + " Укажите другой email!";
            log.error("Update user(id={}) credentials failed: {}",
                            user.getId(), re.getMessage());
            user.setPassword("");
            user.setUpdated(user.getCreated());
            return new ModelAndView("profile/edit-user-credentials-prohibition")
                    .addObject("errorMessage", errorMessage)
                    .addObject("user", user);
        }
        log.info("update of user(id={}) credentials was successful!",
                        user.getId());
        return new ModelAndView("profile/edit-user-credentials-success")
                    .addObject("userName", user.getUsername());
    }

    /**
     * Возвращает вид-подтверждение действий администратору приложения об
     * изменении доступа пользователя в приложение
     * @param userId идентификатор пользователя
     * @return вид по имени admin/customer/change-access-confirm с данными
     * о пользователе
     */
    @GetMapping("/users/{userId}/change_access/confirm")
    public ModelAndView viewChangeUserAccessConfirm(
                                            @PathVariable("userId") int userId) {
        return new ModelAndView("admin/customer/change-access-confirm")
                    .addObject("user", userService.findById(userId));
    }

    /**
     * Изменяет доступ пользователя в приложение
     * ROLE_ADMIN
     * @param userId идентификатор пользователя
     * @param access текущий статус доступа
     * @return вид по имени admin/customer/change-access-success с
     * соответствующим сообщением и статусом доступа
     */
    @PostMapping("/users/{userId}/change_access")
    public ModelAndView changeUserAccess(@PathVariable("userId") int userId,
                                         @RequestParam("enabled") boolean access) {
        User user = userService.findById(userId);
        user.setEnabled(!user.isEnabled());
        userService.update(user);
        String message;
        if (access) {
            message = String.format(
                    "Пользователю (username = %s, id = %d) заблокирован доступ"
                    + " в приложение!", user.getUsername(), user.getId()
            );
            log.info("Administrator has blocked access to the application to"
                            + " the user(username = {}, id = {})",
                                    user.getUsername(), user.getId());
        } else {
            message = String.format(
                    "Пользователю (username = %s, id = %d) восстановлен доступ"
                            + " в приложение!", user.getUsername(), user.getId()
            );
            log.info("Administrator has restored access to the application to"
                            + " the user(username = {}, id = {})",
                                    user.getUsername(), user.getId());
        }
        return new ModelAndView("admin/customer/change-access-success")
                    .addObject("message", message)
                    .addObject("access", access);
    }

    /**
     * Производит поиск пользователя по username в хранилище
     * @param username username пользователя
     * @return вид по имени admin/customer/show-user-data с данными о
     * найденном пользователе, иначе вид admin/customer/no-filtered-user-inform
     * с сообщением, что пользователь не найден
     */
    @GetMapping("/getUserByUsername")
    public ModelAndView getUserByUsername(@RequestParam("username") String username) {
        User user = userService.findByUserName(username);
        String errorMessage = String.format(
                "Пользователя с username [%s] не найдено в базе данных!"
                        + " Уточните значение username!", username
        );
        return user == null
                ? new ModelAndView("admin/customer/no-filtered-user-inform")
                        .addObject("errorMessage", errorMessage)
                : new ModelAndView("admin/customer/show-user-data")
                        .addObject("user", user);
    }
}
