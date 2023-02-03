package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для главной страницы веб-приложения
 */
@Controller
public class IndexController {

    /**
     * Перенаправляет на страницу со всеми нарушениями
     * @return перенаправление на страницу со всеми нарушениями
     * /accidents
     */
    @GetMapping("/index")
    public String index() {
        return "redirect:/accidents";
    }
}
