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
     * Хранилище в виде HashSet
     */
    private final Set<AccidentType> types = new HashSet<>();

    public AccidentTypeMemRepository() {
        init();
    }

    /**
     * Инициализирует хранилище начальными данными
     */
    private void init() {
        types.add(new AccidentType(1, "Две машины"));
        types.add(new AccidentType(2, "Машина и человек"));
        types.add(new AccidentType(3, "Машина и велосипед"));
    }
    @Override
    public AccidentType add(AccidentType accidentType) {
        AccidentType rsl = null;
        if (types.add(accidentType)) {
            rsl = accidentType;
        }
        return rsl;
    }

    @Override
    public boolean update(AccidentType accidentType) {
        types.remove(accidentType);
        return types.add(accidentType);
    }

    @Override
    public AccidentType delete(AccidentType accidentType) {
        AccidentType rsl = null;
        if (types.remove(accidentType)) {
            rsl = accidentType;
        }
        return rsl;
    }

    @Override
    public List<AccidentType> findAll() {
        return new ArrayList<>(types);
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        return types.stream()
                .filter(at -> at.getId() == id)
                .findFirst();
    }
}
