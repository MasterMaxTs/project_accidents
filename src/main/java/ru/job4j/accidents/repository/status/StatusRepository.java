package ru.job4j.accidents.repository.status;

import ru.job4j.accidents.model.Status;
import ru.job4j.accidents.repository.Repository;

/**
 * Хранилище Статусы сопровождения автоинцидентов
 */
public abstract class StatusRepository implements Repository<Status> {

    /**
     * Константа id статуса 'Принят'
     */
    public static final int ACCEPTED_STATUS_ID = 1;

    /**
     * Константа id статуса 'Ожидание'
     */
    public static final int QUEUED_STATUS_ID = 2;

    /**
     * Константа id статуса 'Рассматривается'
     */
    public static final int IN_WORKED_STATUS_ID = 3;

    /**
     * Константа id статуса 'Решён'
     */
    public static final int RESOLVED_STATUS_ID = 4;

    /**
     * Константа id статуса 'Архив'
     */
    public static final int ARCHIVED_STATUS_ID = 5;
}
