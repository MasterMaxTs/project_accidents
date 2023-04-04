package ru.job4j.accidents.repository.status;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.job4j.accidents.model.Status;

/**
 * Доступ к хранилищу Статусов сопровождения инцидентов c помощью Spring Data
 */
public interface StatusPagingAndSortingRepository
                    extends PagingAndSortingRepository<Status, Integer> {
}
