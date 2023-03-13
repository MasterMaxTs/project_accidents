package ru.job4j.accidents.service.user;

import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.Service;

/**
 * Сервис Пользователей
 */
public interface UserService extends Service<User> {

    /**
     * Находит пользователя по имени
     * @param username имя пользователя
     * @return User
     */
    User findByUserName(String username);
}
