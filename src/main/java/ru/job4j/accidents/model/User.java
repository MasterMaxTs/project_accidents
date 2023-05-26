package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
     * Email пользователя
     */
    @NonNull
    private String email;

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
     * Локальное время регистрации пользователя в приложении
     */
    private LocalDateTime created;

    /**
     * Локальное время обновления учётных данных пользователя в приложении
     */
    private LocalDateTime updated;

    /**
     * Список автоинцидентов пользователя
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private List<Accident> accidents;

    /**
     * Список карточек учёта автомобилей пользователя в ГИБДД
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private List<RegistrationCard> registrationCards;
}
