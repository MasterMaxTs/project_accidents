package ru.job4j.accidents.service.accident;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.accident.AccidentCrudRepository;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Автомобильных инцидентов
 */
@Service
@AllArgsConstructor
public class AccidentDataService implements AccidentService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * Автомобильных инцидентов
     */
    private final AccidentCrudRepository store;

    @Override
    public Accident add(Accident accident) {
        return store.save(accident);
    }

    @Override
    public boolean update(Accident accident) {
        store.save(accident);
        return true;
    }

    @Override
    public Accident delete(Accident accident) {
        store.delete(accident);
        return accident;
    }

    /**
     * Возвращает список всех автоинцидентов из БД, отсортированных по
     * локальному времени регистрации, далее по локальному времени обновления
     * от позднего к раннему
     * @return отсортированный список всех автоинцидентов
     */
    @Override
    public List<Accident> findAll() {
        List<Accident> rsl = (List<Accident>) store.findAll();
        rsl.sort(
                Comparator.comparing(Accident::getCreated).reversed()
                        .thenComparing(
                                Comparator.comparing(Accident::getUpdated).reversed()
                        ));
        return rsl;
    }

    @Override
    public Accident findById(int id) {
        return store.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                    String.format("Accident with id = %d not found in store", id)
                ));
    }
}
