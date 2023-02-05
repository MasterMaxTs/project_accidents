package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.service.accident.AccidentService;

/**
 * Контроллер автонарушений
 */
@Controller
@AllArgsConstructor
public class AccidentController {

    /**
     * Ссылка на слой сервисов
     */
    private AccidentService service;

    /**
     * Возращает вид с данными в виде всех нарушений
     * @return вид по имени "index" со всеми нарушениями "accidents"
     * в виде списка
     */
    @GetMapping("/accidents")
    public ModelAndView getAll(@RequestParam("user") String user) {
        return new ModelAndView("index")
                        .addObject("user", user)
                        .addObject("accidents", service.findAll());
    }
}
