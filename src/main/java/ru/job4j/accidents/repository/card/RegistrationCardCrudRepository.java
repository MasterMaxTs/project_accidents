package ru.job4j.accidents.repository.card;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.RegistrationCard;

/**
 * Хранилище Карточек учёта транспортных средств в ГИБДД
 */
public interface RegistrationCardCrudRepository
                        extends CrudRepository<RegistrationCard, Integer> {
    /**
     * Находит регистрационную карточку учёта автомобиля пользователя по
     * государственному регистрационному знаку (ГРЗ) автомобиля
     * @param carPlate ГРЗ автомобиля
     * @return регистрационную карточку учёта автомобиля пользователя
     */
    RegistrationCard findByCarPlate(String carPlate);
}
