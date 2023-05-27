package ru.job4j.accidents.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер начальной страницы
 */
@Controller
@Slf4j
public class IndexController {

    /**
     * Перенаправляет на страницу аутентификации
     * @return редирект на страницу аутентификации
     */
    @GetMapping("/index")
    public String index() {
        log.info("Trying to login to the app from the index page");
        return "redirect:/login";
    }
}
