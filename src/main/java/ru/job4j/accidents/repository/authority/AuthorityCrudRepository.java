package ru.job4j.accidents.repository.authority;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Authority;

/**
 * Доступ к хранилищу Ролей пользователей
 */
public interface AuthorityCrudRepository
                                extends CrudRepository<Authority, Integer> {

    /**
     * Находит роль пользователя по имени
     * @param authority имя роли
     * @return Authority
     */
    Authority findByAuthority(String authority);
}
