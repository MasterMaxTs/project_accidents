package ru.job4j.accidents.repository.status;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Status;

import java.util.*;

/**
 * Реализация локального хранилища Статусов сопровождения автоинцидентов
 */
@Repository
public class MemStatusRepository extends StatusRepository {

    /**
     * Список статусов в виде HashMap
     */
    private final Map<Integer, Status> statuses = new HashMap<>();

    /**
     * Конструктор
     */
    public MemStatusRepository() {
        init();
    }

    /**
     * Инициализация локального хранилища начальными данными
     */
    private void init() {
        statuses.put(1, new Status(ACCEPTED_STATUS_ID, "Принят"));
        statuses.put(2, new Status(QUEUED_STATUS_ID, "Ожидание"));
        statuses.put(3, new Status(IN_WORKED_STATUS_ID, "Рассматривается"));
        statuses.put(4, new Status(RESOLVED_STATUS_ID, "Решён"));
        statuses.put(5, new Status(ARCHIVED_STATUS_ID, "Архив"));
    }

    @Override
    public Optional<Status> findById(int id) {
        return Optional.ofNullable(statuses.get(id));
    }

    @Override
    public Status add(Status status) {
        return statuses.putIfAbsent(status.getId(), status);
    }

    @Override
    public boolean update(Status status) {
        statuses.replace(status.getId(), status);
        return true;
    }

    @Override
    public Status delete(Status status) {
        statuses.remove(status.getId());
        return status;
    }

    @Override
    public List<Status> findAll() {
        return new ArrayList<>(statuses.values());
    }
}
