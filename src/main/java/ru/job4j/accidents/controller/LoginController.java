package ru.job4j.accidents.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Контроллер аутентификации и logout
 */
@Controller
@Slf4j
public class LoginController {

    /**
     * Возращает вид-форму с для аутентификации,
     * возвращает сообщение пользователю
     * @param error значение параметра в виде сообщения  об ошибке
     * аутентификации
     * @param logout значение параметра, сигнализирующего о выходе пользователя
     * из приложения
     * @return вид по имени "login/login" с сообщением
     */
    @GetMapping("/login")
    public ModelAndView loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username или Password не корректны !!!";
        }
        if (logout != null) {
            errorMessage = "Вы успешно вышли из приложения !!!";
        }
        return new ModelAndView("login/login")
                .addObject("errorMessage", errorMessage);
    }

    /**
     * Перенаправляет запрос на страницу для аутентификации пользователя
     * после процедуры logout
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param auth Authentication
     * @return перенаправляет запрос на страницу для аутентификации пользователя
     * с параметром logout=true
     */
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest req,
                             HttpServletResponse res,
                             Authentication auth) {
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(req, res, auth);
            log.info("User with username={} successfully logged out", auth.getName());
        }
        return "redirect:/login?logout=true";
    }
}
