package ru.job4j.accidents.repository.filter;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Класс подключает DelegatingFilterProxy - главный фильтр, в котором идёт
 * обработка запросов.
 */
public class SecurityInit extends AbstractSecurityWebApplicationInitializer {
}
