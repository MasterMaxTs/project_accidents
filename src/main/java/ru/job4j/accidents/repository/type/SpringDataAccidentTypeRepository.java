package ru.job4j.accidents.repository.type;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.AccidentType;

/**
 * Доступ к хранилищу типоа автомобильных инцидентов c помощью Spring Data
 */
public interface SpringDataAccidentTypeRepository
                            extends CrudRepository<AccidentType, Integer> {
}
