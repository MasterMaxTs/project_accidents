package ru.job4j.accidents.model;

import lombok.*;

/**
 * Модель данных Статья нарушения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rule {

    /**
     * Идентификатор статьи
     */
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Описание статьи
     */
    private String name;

    /**
     * Переопределяет метод toString()
     * @return название статьи нарушения
     */
    @Override
    public String toString() {
        return name;
    }
}
