package ru.job4j.accidents.service.rule;

import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.Service;

import java.util.List;

/**
 * Сервис Статей автоинцидентов
 */
public interface RuleService extends Service<Rule> {

    /**
     * Возвращает список уникальных статей из массива их идентификаторов
     * @param rulesIDs строковый массив идентификаторов статей автонарушений
     * @return список статей
     */
    List<Rule> getRulesFromIds(String[] rulesIDs);
}
