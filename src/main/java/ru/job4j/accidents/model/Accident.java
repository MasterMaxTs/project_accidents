package ru.job4j.accidents.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Модель данных Автоинцидент
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Accident {

    /**
     * Идентификатор автоинцидента
     */
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Название
     */
    @NonNull
    private String name;

    /**
     * Тип автоинцидента
     */
    @NonNull
    private AccidentType type;

    /**
     * Описание
     */
    @NonNull
    private String text;

    /**
     * Список статей автонарушений
     */
    @NonNull
    private Set<Rule> rules;

    /**
     * Адрес
     */
    @NonNull
    private String address;

    /**
     * Локальное время создания автонинцидента
     */
    private LocalDateTime created;

    /**
     * Локальное время обновления автонинцидента
     */
    private LocalDateTime updated;

}
