package ru.job4j.accidents.service.card;

import ru.job4j.accidents.model.RegistrationCard;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.service.Service;

import java.util.List;

/**
 * Сервис Учёта транспортных средств в ГИБДД
 */
public interface RegistrationCardService extends Service<RegistrationCard> {

    /**
     * Получает список учётных карточек автомобилей пользователя из
     * параметров запроса
     * @param user пользователь - владелец автомобилей
     * @param card учётная карточка автомобиля пользователя с частично
     * заполненными данными на входе
     * @param carPlates массив регистрационных знаков автомобилей пользователя
     * @param certificateNumbers массив номеров свидетельств регистраций
     * автомобилей пользователя
     * @param vinCodes массив значений VIN-кодов автомобилей пользователя
     * @return список учётных карточек автомобилей пользователя
     */
    List<RegistrationCard> collectRegistrationCardsFromRequest(User user,
                                                               RegistrationCard card,
                                                               String[] carPlates,
                                                               String[] certificateNumbers,
                                                               String[] vinCodes);
    /**
     * Находит регистрационную карточку учёта автомобиля пользователя по
     * государственному регистрационному знаку (ГРЗ) автомобиля
     * @param carPlate ГРЗ автомобиля
     * @return регистрационную карточку учёта автомобиля пользователя
     */
    RegistrationCard findByCarPlate(String carPlate);
}
