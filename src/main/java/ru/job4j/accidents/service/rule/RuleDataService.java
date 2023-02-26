package ru.job4j.accidents.service.rule;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.rule.RulePagingAndSortingRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Реализация сервиса Статей автонарушений
 */
@Service
@AllArgsConstructor
public class RuleDataService implements RuleService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * Статей автонарушений
     */
    private final RulePagingAndSortingRepository store;

    @Override
    public Rule add(Rule rule) {
        return store.save(rule);
    }

    @Override
    public boolean update(Rule rule) {
        store.save(rule);
        return true;
    }

    @Override
    public Rule delete(Rule rule) {
        store.delete(rule);
        return rule;
    }

    /**
     * Возвращает список всех статей из БД, отсортированных по имени
     * @return отсортированный список всех статей автонарушений
     */
    @Override
    public List<Rule> findAll() {
        return (List<Rule>) store.findAll(Sort.by("name"));
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
                .map(rId -> findById(Integer.parseInt(rId)))
                .collect(Collectors.toList());
    }
}
