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
     * Статус сопровождения Принят
     */
    ACCEPTED_STATUS(1),

    /**
     * Статус сопровождения Скорректирован
     */
    ADJUSTED_STATUS(2),

    /**
     * Статус сопровождения Ожидание
     */
    QUEUED_STATUS(3),

    /**
     * Статус сопровождения Рассматривается
     */
    IN_WORKED_STATUS(4),

    /**
     * Статус сопровождения Решён
     */
    RESOLVED_STATUS(5),

    /**
     * Статус сопровождения Архив
     */
    ARCHIVED_STATUS(6),

    /**
     * Статус сопровождения Возвращён
     */
    RETURNED_STATUS(7);

    /**
     * Идентификатор статуса сопровождения
     */
    private final int id;
}
