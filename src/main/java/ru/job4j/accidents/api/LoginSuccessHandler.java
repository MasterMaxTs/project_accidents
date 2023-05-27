package ru.job4j.accidents.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс используется для настройки обработчика запроса при успешной
 * авторизации пользователя.
 */
@Component
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * Имя роли администратора приложения
     */
    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";

    /**
     * Имя роли обычного пользователя приложения
     */
    private static final String USER_ROLE_NAME = "ROLE_USER";

    /**
     * Редирект на начальную страницу для авторизовавшегося пользователя
     * ROLE_ADMIN
     */
    private static final String REDIRECT_URL_FOR_ADMIN_ROLE = "/getAllAccidents";

    /**
     * Редирект на начальную страницу для авторизовавшегося пользователя
     * ROLE_USER
     */
    private static final String REDIRECT_URL_FOR_USER_ROLE = "/getAllByUser";

    /**
     * Поручает Spring Security перенаправлять входящий запрос от успешно
     * прошедшего аутентификацию пользователя на соответствующий URL,
     * взависимости от его роли в приложении
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param authentication Authentication
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String redirectURL = request.getContextPath();
        if (userDetails.getAuthorities()
                .contains(new SimpleGrantedAuthority(ADMIN_ROLE_NAME))) {
            log.info("User with username={} has successfully logged",
                                                    authentication.getName());
            redirectURL = REDIRECT_URL_FOR_ADMIN_ROLE;
        } else if (userDetails.getAuthorities()
                .contains(new SimpleGrantedAuthority(USER_ROLE_NAME))) {
            log.info("User with username={} has successfully logged",
                                                     authentication.getName());
            redirectURL = REDIRECT_URL_FOR_USER_ROLE;
        }
        response.sendRedirect(redirectURL);
    }
}
