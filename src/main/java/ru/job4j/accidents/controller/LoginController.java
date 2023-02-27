package ru.job4j.accidents.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class LoginController {

    /**
     * Возращает вид с данными для аутентификации
     * @return вид по имени "login/login"
     */
    @GetMapping("/login")
    public ModelAndView loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username or Password is incorrect !!!";
        }
        if (logout != null) {
            errorMessage = "You have been successfully logged out !!!";
        }
        return new ModelAndView("login/login")
                .addObject("errorMessage", errorMessage);
    }

    /**
     * Перенаправляет запрос на страницу для аутентификации пользователя
     * после процедуры logout
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return перенаправляет запрос на страницу для аутентификации пользователя
     * с параметром logout=true
     */
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest req, HttpServletResponse res) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(req, res, auth);
        }
        return "redirect:/login?logout=true";
    }
}
