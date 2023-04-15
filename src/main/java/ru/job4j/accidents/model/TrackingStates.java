package ru.job4j.accidents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Модель данных Состояние статуса сопровождения автоинцидентов
 */
@AllArgsConstructor
@Getter
public enum TrackingStates {

    /**
     * Константы состояний
     */
    ACCEPTED_STATUS(1),
    QUEUED_STATUS(2),
    IN_WORKED_STATUS(3),
    RESOLVED_STATUS(4),
    ARCHIVED_STATUS(5);

    /**
     * Идентификатор состояния
     */
    private final int id;
}
