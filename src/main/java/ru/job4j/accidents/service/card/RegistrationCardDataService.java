package ru.job4j.accidents.service.card;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.RegistrationCard;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.card.RegistrationCardCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Учёта транспортных средств в ГИБДД
 */
@Service
@AllArgsConstructor
public class RegistrationCardDataService implements RegistrationCardService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * Карточек учёта транспортных средств
     */
    private final RegistrationCardCrudRepository store;

    @Override
    public RegistrationCard add(RegistrationCard card) {
        store.save(card);
        return card;
    }

    @Override
    public boolean update(RegistrationCard card) {
        store.save(card);
        return true;
    }

    @Override
    public RegistrationCard delete(RegistrationCard card) {
        store.delete(card);
        return card;
    }

    @Override
    public List<RegistrationCard> findAll() {
        return (List<RegistrationCard>) store.findAll();
    }

    @Override
    public RegistrationCard findById(int id) {
        return store.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                String.format("Registration card with id = %d"
                                        + " not found in store", id)
                        ));
    }
    @Override
    public List<RegistrationCard> collectRegistrationCardsFromRequest(
                                                    User user,
                                                    RegistrationCard card,
                                                    String[] carPlates,
                                                    String[] certificateNumbers,
                                                    String[] vinCodes) {
        List<RegistrationCard> rsl = new ArrayList<>();
        for (int i = 0; i < vinCodes.length; i++) {
            RegistrationCard rc = new RegistrationCard(card.getOwnerName(),
                                                       card.getOwnerSurname(),
                                                       carPlates[i],
                                                       certificateNumbers[i],
                                                       vinCodes[i]
            );
            rc.setUser(user);
            rsl.add(rc);
        }
        return rsl;
    }

    @Override
    public RegistrationCard findByCarPlate(String carPlate) {
        return store.findByCarPlate(carPlate);
    }
}
