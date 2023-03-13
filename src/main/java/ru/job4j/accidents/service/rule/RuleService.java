package ru.job4j.accidents.service.rule;

import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Сервис Статей автоинцидентов
 */
public interface RuleService extends Service<Rule> {

    /**
     * Возвращает список уникальных статей из массива их идентификаторов
     * @param req HttpServletRequest
     * @param parameter строковый массив идентификаторов статей
     * @return список уникальных статей
     */
    List<Rule> getRulesFromIds(HttpServletRequest req, String parameter);
}
