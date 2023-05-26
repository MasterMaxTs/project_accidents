package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель данных Карточка учёта автомобиля в ГИБДД
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "registration_cards")
public class RegistrationCard {

    /**
     * Идентификатор карточки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя владельца автомобиля
     */
    @NonNull
    @Column(name = "owner_name")
    private String ownerName;

    /**
     * Фамилия владельца автомобиля
     */
    @NonNull
    @Column(name = "owner_surname")
    private String ownerSurname;

    /**
     * Регистрационный знак автомобиля
     */
    @NonNull
    @Column(name = "car_plate")
    private String carPlate;

    /**
     * Номер свидетельства регистрации автомобиля
     */
    @NonNull
    @Column(name = "registration_certificate_number")
    private String certificateNumber;

    /**
     * VIN-код автомобиля
     */
    @NonNull
    @Column(name = "vin_code")
    private String vinCode;

    /**
     * Пользователь - владелец автомобиля
     */
    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;
}
