package ru.job4j.accidents.repository.user;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.User;

/**
 * Доступ к хранилищу Пользователей
 */
public interface UserCrudRepository extends CrudRepository<User, Integer> {

    /**
     * Находит пользователя по имени
     * @param userName имя пользователя
     * @return User
     */
    User findByUsername(String userName);
}
