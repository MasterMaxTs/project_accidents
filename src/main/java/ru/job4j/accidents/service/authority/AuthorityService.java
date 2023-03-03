package ru.job4j.accidents.service.authority;

import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.service.Service;

/**
 * Сервис Ролей пользователей
 */
public interface AuthorityService extends Service<Authority> {

    /**
     * Находит роль пользователя по имени
     * @param authority имя роли
     * @return Authority
     */
    Authority findByAuthority(String authority);
}
