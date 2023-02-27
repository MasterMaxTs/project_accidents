package ru.job4j.accidents.service.rule;

import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.Service;

import java.util.List;

/**
 * Сервис Статей автонарушений
 */
public interface RuleService extends Service<Rule> {

    /**
     * Возвращает множество уникальных статей из массива их идентификаторов
     * @param rIds строковый массив идентификаторов статей
     * @return множество уникальных статей
     */
    List<Rule> getRulesFromIds(String[] rIds);
}