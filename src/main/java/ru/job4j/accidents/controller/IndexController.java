package ru.job4j.accidents.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер главной страницы веб-приложения
 */
@Controller
public class IndexController {

    /**
     * Перенаправляет на соответствующую страницу со всеми автоинцидентами
     * в зависимости от роли авторизовавшегося в приложении пользователя
     * @return перенаправление на соответствующую начальную страницу
     * /getAllAccidents - for ADMIN, /getAllByUser - for USER
     */
    @GetMapping("/index")
    public String index() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Object[] authorities = authentication.getAuthorities().toArray();
        String authority = authorities[authorities.length - 1].toString();
        return authority.equals("ROLE_ADMIN") ? "redirect:/getAllAccidents"
                : "redirect:/getAllByUser";
    }
}
