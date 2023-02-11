package ru.job4j.accidents.model;

import lombok.*;

/**
 * Модель данных автоинцидент
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
     * Адрес
     */
    @NonNull
    private String address;
}
