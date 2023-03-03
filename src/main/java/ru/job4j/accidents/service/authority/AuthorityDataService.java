package ru.job4j.accidents.service.authority;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.repository.authority.AuthorityCrudRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Ролей пользователей
 */
@Service
@AllArgsConstructor
public class AuthorityDataService implements AuthorityService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * ролей пользователей
     */
    private final AuthorityCrudRepository store;

    @Override
    public Authority add(Authority authority) {
        store.save(authority);
        return authority;
    }

    @Override
    public boolean update(Authority authority) {
        store.save(authority);
        return true;
    }

    @Override
    public Authority delete(Authority authority) {
        store.delete(authority);
        return authority;
    }

    @Override
    public List<Authority> findAll() {
        return (List<Authority>) store.findAll();
    }

    @Override
    public Authority findById(int id) {
        return store.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        String.format("Authority with id = %d not found in store", id)
                ));
    }

    @Override
    public Authority findByAuthority(String authority) {
        return store.findByAuthority(authority);
    }
}
