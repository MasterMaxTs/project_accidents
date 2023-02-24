package ru.job4j.accidents.service.accident.type;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.accident.type.AccidentTypeRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса типа автомобильных инцидентов
 */
@Service
public class AccidentTypeServiceImpl implements AccidentTypeService {

    /**
     * Делегирование выполнения операций хранилищу типов автомобильных
     * инцидентов
     */
    private final AccidentTypeRepository store;

    /**
     * Конструктор
     * @param store внедрение зависимости AccidentTypeRepository с уточнением
     * бина реализации
     */
    public AccidentTypeServiceImpl(@Qualifier("hibernateAccidentTypeRepository")
                                   AccidentTypeRepository store) {
        this.store = store;
    }

    @Override
    public AccidentType add(AccidentType accidentType) {
        return store.add(accidentType);
    }

    @Override
    public boolean update(AccidentType accidentType) {
        return store.update(accidentType);
    }

    @Override
    public AccidentType delete(AccidentType accidentType) {
        return store.delete(accidentType);
    }

    @Override
    public List<AccidentType> findAll() {
        return store.findAll();
    }

    @Override
    public AccidentType findById(int id) {
        return store.findById(id)
                    .orElseThrow(
                            () -> new NoSuchElementException(
                    String.format("Accident with id = %d not found in store", id)
        ));
    }
}
