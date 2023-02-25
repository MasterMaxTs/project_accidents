package ru.job4j.accidents.repository.rule;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Rule;

/**
 * Доступ к хранилищу статей автонарушений c помощью Spring Data
 */
public interface SpringDataRuleRepository
                                    extends CrudRepository<Rule, Integer> {
}
