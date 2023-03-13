package ru.job4j.accidents.service.status;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Status;
import ru.job4j.accidents.repository.rule.RulePagingAndSortingRepository;
import ru.job4j.accidents.repository.status.StatusPagingAndSortingRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Статусов сопровождения автоинцидентов
 */
@Service
@AllArgsConstructor
public class StatusDataService implements StatusService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * Статусов сопровождения автоинцидентов
     */
    private final StatusPagingAndSortingRepository store;

    @Override
    public Status add(Status status) {
        return store.save(status);
    }

    @Override
    public boolean update(Status status) {
        store.save(status);
        return true;
    }

    @Override
    public Status delete(Status status) {
        store.delete(status);
        return status;
    }

    @Override
    public List<Status> findAll() {
        return (List<Status>) store.findAll(Sort.by("id"));
    }

    @Override
    public Status findById(int id) {
        return store.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        String.format("Status with id = %d not found in store", id)
                ));
    }
}
