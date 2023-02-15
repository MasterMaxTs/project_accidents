package ru.job4j.accidents.repository.accident.type;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.*;

/**
 * Реализация  локального хранилища типа автомобильных инцидентов
 */
@Repository
public class AccidentTypeMemRepository implements AccidentTypeRepository {

    /**
     * Хранилище в виде HashMap
     */
    private final Map<Integer, AccidentType> types = new HashMap<>();

    public AccidentTypeMemRepository() {
        init();
    }

    /**
     * Инициализирует хранилище начальными данными
     */
    private void init() {
        add(new AccidentType(1, "Две машины"));
        add(new AccidentType(2, "Машина и человек"));
        add(new AccidentType(3, "Машина и велосипед"));
    }
    @Override
    public AccidentType add(AccidentType accidentType) {
        return types.putIfAbsent(accidentType.getId(), accidentType);
    }

    @Override
    public boolean update(AccidentType accidentType) {
        return types.replace(accidentType.getId(), accidentType) != null;
    }

    @Override
    public AccidentType delete(AccidentType accidentType) {
        return types.remove(accidentType.getId());
    }

    @Override
    public List<AccidentType> findAll() {
        return new ArrayList<>(types.values());
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        return Optional.ofNullable(types.get(id));
    }
}
