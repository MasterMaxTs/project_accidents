package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Модель данных Автоинцидент
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "accidents")
public class Accident {

    /**
     * Идентификатор автоинцидента
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne
    @JoinColumn(name = "type_id")
    private AccidentType type;

    /**
     * Описание
     */
    @NonNull
    private String text;

    /**
     * Автор инцидента
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    /**
     * Список статей автонарушений
     */
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "accidents_rules",
            joinColumns = @JoinColumn(name = "accident_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id")
    )
    private List<Rule> rules;

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

    /**
     * Статус решения автоинцидента
     */
    @NonNull
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    /**
     * Постановление о ДТП
     */
    private String resolution;

}
