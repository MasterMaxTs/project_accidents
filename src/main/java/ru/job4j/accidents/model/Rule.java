package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель данных Статья нарушения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "rules")
public class Rule {

    /**
     * Идентификатор статьи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
