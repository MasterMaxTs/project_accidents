package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель данных Роль пользователя
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "authorities")
public class Authority {

    /**
     * Идентификатор роли
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Название
     */
    @NonNull
    private String authority;
}
