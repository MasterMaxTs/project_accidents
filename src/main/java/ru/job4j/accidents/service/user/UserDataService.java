package ru.job4j.accidents.service.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.user.UserCrudRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Пользователей
 */
@Service
@AllArgsConstructor
public class UserDataService implements UserService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * пользователей
     */
    private final UserCrudRepository store;

    @Override
    public User add(User user) {
        store.save(user);
        return user;
    }

    @Override
    public boolean update(User user) {
        store.save(user);
        return true;
    }

    @Override
    public User delete(User user) {
        store.delete(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return (List<User>) store.findAll(
                Sort.by("created").descending()
                        .and(Sort.by("enabled").descending())
        );
    }

    @Override
    public User findById(int id) {
        return store.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        String.format("User with id = %d not found in store", id)
                ));
    }

    @Override
    public User findByUserName(String username) {
        return store.findByUsername(username);
    }
}
