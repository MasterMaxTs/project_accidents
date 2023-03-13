package ru.job4j.accidents.repository.user;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.User;

/**
 * Доступ к хранилищу Пользователей
 */
public interface UserCrudRepository extends CrudRepository<User, Integer> {

    /**
     * Проверяет, существует ли пользователь в БД с указанным именем
     * @param username имя пользователя
     * @return true, если пользователь найден в БД, иначе - false
     */
    boolean existsByUsername(String username);

    User findByUsername(String userName);
}
