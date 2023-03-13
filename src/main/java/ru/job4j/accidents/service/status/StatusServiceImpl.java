package ru.job4j.accidents.service.status;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Status;
import ru.job4j.accidents.repository.status.StatusRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Статусов сопровождения автоинцидентов
 */
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository store;

    public StatusServiceImpl(@Qualifier("jdbcTemplateStatusRepository")
                             StatusRepository store) {
        this.store = store;
    }

    @Override
    public Status add(Status status) {
        return store.add(status);
    }

    @Override
    public boolean update(Status status) {
        return store.update(status);
    }

    @Override
    public Status delete(Status status) {
        return store.delete(status);
    }

    @Override
    public Status findById(int id) {
        return store.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                String.format("Status with id = %d not found in store", id)
                        ));
    }

    @Override
    public List<Status> findAll() {
        return store.findAll();
    }
}
