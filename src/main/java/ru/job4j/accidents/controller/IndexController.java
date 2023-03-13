package ru.job4j.accidents.controller;

import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для главной страницы веб-приложения
 */
@Controller
public class IndexController {

    /**
     * Перенаправляет на соответствующую страницу со всеми автоинцидентами
     * в зависимости от роли авторизовавшегося в приложении пользователя
     * @param request SecurityContextHolderAwareRequestWrapper
     * @return перенаправление на соответствующую начальную страницу
     * /getAllAccidents - for ROLE_ADMIN, /getAllByUser - for ROLE_USER
     */
    @GetMapping("/index")
    public String index(SecurityContextHolderAwareRequestWrapper request) {
        return request.isUserInRole("ROLE_ADMIN") ? "redirect:/getAllAccidents"
                : "redirect:/getAllByUser";
    }
}
