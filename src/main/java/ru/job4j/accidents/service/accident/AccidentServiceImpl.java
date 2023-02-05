package ru.job4j.accidents.service.accident;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.accident.AccidentRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Автомобильных инцидентов
 */
@Service
@AllArgsConstructor
public class AccidentServiceImpl implements AccidentService {

    /**
     * Делегирование выполнения операций хранилищу Автомобильных инцидентов
     */
    private final AccidentRepository store;

    @Override
    public Accident add(Accident accident) {
        return store.add(accident);
    }

    @Override
    public boolean update(Accident accident) {
        return store.update(accident);
    }

    @Override
    public Accident delete(Accident accident) {
        return store.delete(accident);
    }

    @Override
    public List<Accident> findAll() {
        return store.findAll();
    }

    @Override
    public Accident findById(int id) {
        return store.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Accident with id = %d not found in store", id)
                ));
    }
}
