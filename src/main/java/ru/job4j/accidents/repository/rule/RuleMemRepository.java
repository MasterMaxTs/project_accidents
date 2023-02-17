package ru.job4j.accidents.repository.rule;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.*;

/**
 * Реализация локального хранилища Статей автонарушений
 */
@Repository
public class RuleMemRepository implements RuleRepository {

    /**
     * Список статей в виде HashSet
     */
    private final Set<Rule> rules = new HashSet<>();

    public RuleMemRepository() {
        init();
    }

    /**
     * Инициализация локального хранилища начальными данными
     */
    private void init() {
        rules.add(new Rule(1, "Статья. 1"));
        rules.add(new Rule(2, "Статья. 2"));
        rules.add(new Rule(3, "Статья. 3"));
        rules.add(new Rule(4, "Статья. 4"));
    }

    @Override
    public Rule add(Rule rule) {
        return rules.add(rule) ? rule : null;
    }

    @Override
    public boolean update(Rule rule) {
        return rules.remove(rule) && rules.add(rule);
    }

    @Override
    public Rule delete(Rule rule) {
        return rules.remove(rule) ? rule : null;
    }

    @Override
    public List<Rule> findAll() {
        return new ArrayList<>(rules);
    }

    @Override
    public Optional<Rule> findById(int id) {
        return rules.stream()
                .filter(r -> r.getId() == id)
                .findFirst();
    }
}
