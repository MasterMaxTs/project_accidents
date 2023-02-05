package ru.job4j.accidents.model;

import lombok.*;

/**
 * Модель данных автонарушение
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Accident {

    /**
     * Идентификатор автонарушения
     */
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя нарушителя
     */
    @NonNull
    private String name;

    /**
     * Описание нарушения
     */
    @NonNull
    private String text;

    /**
     * Адрес
     */
    @NonNull
    private String address;
}
