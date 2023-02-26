package ru.job4j.accidents.service.type;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.type.AccidentTypePagingAndSortingRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса типа автомобильных инцидентов
 */
@Service
@AllArgsConstructor
public class AccidentTypeDataService implements AccidentTypeService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * Типов автомобильных автоинцидентов
     */
    private final AccidentTypePagingAndSortingRepository store;

    @Override
    public AccidentType add(AccidentType type) {
        return store.save(type);
    }

    @Override
    public boolean update(AccidentType type) {
        store.save(type);
        return true;
    }

    @Override
    public AccidentType delete(AccidentType type) {
        store.delete(type);
        return type;
    }

    /**
     * Возвращает список всех типов автоинцидентов из БД,
     * отсортированных по имени
     * @return отсортированный список всех типов автоинцидентов
     */
    @Override
    public List<AccidentType> findAll() {
        return (List<AccidentType>) store.findAll(Sort.by("name"));
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
