package ru.job4j.accidents.service.user;

import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.Service;

/**
 * Сервис Пользователей
 */
public interface UserService extends Service<User> {

    /**
     * Проверяет, есть ли пользователь в БД с указанным именем
     * @param username имя пользователя
     * @return true, если пользователь найден в БД, иначе - false
     */
    boolean existsByUsername(String username);

    /**
     * Находит пользователя по имени
     * @param username имя пользователя
     * @return User
     */
    User findByUsername(String username);
}
