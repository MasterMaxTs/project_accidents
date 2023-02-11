package ru.job4j.accidents.model;

import lombok.*;

/**
 * Модель данных тип автоиницидента
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccidentType {

    /**
     * Идентификатор типа
     */
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Название автоинцидента
     */
    private String name;
}
