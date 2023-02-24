package ru.job4j.accidents.service.rule;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.rule.RuleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Реализация сервиса Статей автонарушений
 */
@Service
public class RuleServiceImpl implements RuleService {

    /**
     * Делегирование выполнения операций хранилищу статей автонарушений
     */
    private final RuleRepository store;

    /**
     * Конструктор
     * @param store внедрение зависимости RuleRepository с уточнением
     * бина реализации
     */
    public RuleServiceImpl(@Qualifier("hibernateRuleRepository")
                           RuleRepository store) {
        this.store = store;
    }

    @Override
    public Rule add(Rule rule) {
        return store.add(rule);
    }

    @Override
    public boolean update(Rule rule) {
        return store.update(rule);
    }

    @Override
    public Rule delete(Rule rule) {
        return store.delete(rule);
    }

    @Override
    public List<Rule> findAll() {
        return store.findAll();
    }

    @Override
    public Rule findById(int id) {
        return store.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                String.format("Rule with id = %d not found in store", id)
                ));
    }

    @Override
    public List<Rule> getRulesFromIds(String[] rIds) {
        return Arrays.stream(rIds)
                .map(sid -> findById(Integer.parseInt(sid)))
                .collect(Collectors.toList());
    }
}
