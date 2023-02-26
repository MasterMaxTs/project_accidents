package ru.job4j.accidents.repository.rule;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.job4j.accidents.model.Rule;

/**
 * Доступ к хранилищу статей автонарушений c помощью Spring Data
 */
public interface RulePagingAndSortingRepository
                        extends PagingAndSortingRepository<Rule, Integer> {
}
