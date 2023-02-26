package ru.job4j.accidents.repository.accident;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.job4j.accidents.model.Accident;

/**
 * Доступ к хранилищу автомобильных инцидентов c помощью Spring Data
 */
public interface AccidentPagingAndSortingRepository extends
                             PagingAndSortingRepository<Accident, Integer> {
}
