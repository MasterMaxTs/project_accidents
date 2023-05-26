package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель данных Статус сопровождения автоинцидента
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "statuses")
public class Status {

    /**
     * Идентификатор статуса
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Название
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
