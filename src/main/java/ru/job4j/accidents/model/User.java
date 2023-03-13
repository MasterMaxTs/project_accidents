package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Модель данных Пользователь
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {

    /**
     * Идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя пользователя
     */
    @NonNull
    private String username;

    /**
     * Пароль пользователя
     */
    @NonNull
    private String password;

    /**
     * Статус доступа в приложение
     */
    private boolean enabled;

    /**
     * Роль пользователя
     */
    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "authority_id")
    private Authority authority;

    /**
     * Список автоинцидентов пользователя
     */
    @OneToMany (
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private List<Accident> accidents;
}
