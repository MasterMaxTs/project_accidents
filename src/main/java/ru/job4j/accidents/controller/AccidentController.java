package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Контроллер автонарушений
 */
@Controller
public class AccidentController {

    /**
     * Возращает вид с данными в виде всех нарушений
     * @return вид по имени "index" со всеми нарушениями "accidents"
     * в виде списка
     */
    @GetMapping("/accidents")
    public ModelAndView getAll() {
        return new ModelAndView("index")
                    .addObject("accidents", List.of());
    }
}
