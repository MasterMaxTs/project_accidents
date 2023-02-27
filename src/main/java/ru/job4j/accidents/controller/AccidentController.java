package ru.job4j.accidents.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.rule.RuleService;
import ru.job4j.accidents.service.type.AccidentTypeService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

/**
 * Контроллер автонарушений
 */
@Controller
public class AccidentController {

    /**
     * Ссылки на слои сервисов
     */
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;
    private final RuleService ruleService;

    /**
     * Конструктор
     * @param accidentService сервис автоинцидентов
     * @param accidentTypeService сервис типов автоинцидентов
     * @param ruleService сервис статей автонарушений
     */
    public AccidentController(@Qualifier("accidentDataService")
                              AccidentService accidentService,
                              @Qualifier("accidentTypeDataService")
                              AccidentTypeService accidentTypeService,
                              @Qualifier("ruleDataService")
                              RuleService ruleService) {
        this.accidentService = accidentService;
        this.accidentTypeService = accidentTypeService;
        this.ruleService = ruleService;
    }

    /**
     * Добавляет объект авторизовавшегося в приложении пользователя
     * во все модели контроллера
     * @param auth Authentication
     * @return объект авторизовавшегося пользователя
     */
    @ModelAttribute
    public Object user(Authentication auth) {
        return auth.getPrincipal();
    }

    /**
     * Возращает вид с данными в виде всех автоинцидентов
     * @return вид по имени "index" со всеми нарушениями "accidents"
     * в виде списка
     */
    @GetMapping("/getAllAccidents")
    public ModelAndView getAll() {
        return new ModelAndView("index/index")
                        .addObject("accidents", accidentService.findAll());
    }

    /**
     * Возращает вид для создания нового автоинцидента с сопутствующими
     * данными в виде списков
     * @return вид по имени "accident/create-accident"
     * в виде формы вместе с сопутствующими данными в виде списков
     */
    @GetMapping("/createAccident")
    public ModelAndView viewCreateAccident(@ModelAttribute Accident accident) {
        return new ModelAndView("accident/create-accident")
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("rules", ruleService.findAll());
    }

    /**
     * Возращает вид для обновления сведений об автоинциденте
     * @return вид по имени "accident/create-accident" с заполненными полями
     * формы начальными данными
     * @throws NoSuchElementException - если автоинцидент
     * не найден в хранилище
     */
    @GetMapping("/accidents/{accidentId}/edit")
    public ModelAndView viewUpdateAccident(@PathVariable("accidentId") int id) {
        return new ModelAndView("accident/create-accident")
                    .addObject("accident", accidentService.findById(id))
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("rules", ruleService.findAll())
                    .addObject("update", true);
    }

    /**
     * Сохраняет автоинцидент в хранилище
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @param request HttpServletRequest
     * @return вид с информацией об успешном создании нового автоинцидента
     * @throws NoSuchElementException - если тип автоиницидента
     * не найден в хранилище
     */
    @PostMapping("/saveAccident")
    public ModelAndView save(@ModelAttribute Accident accident,
                             @RequestParam("type.id") int typeId,
                             HttpServletRequest request) {
        String[] rIds = request.getParameterValues("rIds");
        accident.setType(accidentTypeService.findById(typeId));
        accident.setRules(ruleService.getRulesFromIds(rIds));
        accident.setCreated(LocalDateTime.now());
        accidentService.add(accident);
        System.out.println("Accident created successfully");
        return new ModelAndView("accident/create-accident-success");
    }

    /**
     * Обновляет данные автоинцидента в хранилище
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @param request HttpServletRequest
     * @return вид с информацией об успешном обновлении данных автоинцидента
     * @throws NoSuchElementException - если тип автоиницидента
     * не найден в хранилище
     */
    @PostMapping("/updateAccident")
    public ModelAndView update(@ModelAttribute Accident accident,
                               @RequestParam("type.id") int typeId,
                               HttpServletRequest request) {
        String[] rIds = request.getParameterValues("rIds");
        accident.setType(accidentTypeService.findById(typeId));
        accident.setRules(ruleService.getRulesFromIds(rIds));
        accident.setUpdated(LocalDateTime.now());
        accidentService.update(accident);
        System.out.println("Accident updated successfully");
        return new ModelAndView("accident/update-accident-success");
    }

    /**
     * Возвращает вид с информацией выбранного автоинцидента
     * @param id идентификатор автоинцидента
     * @return возвращает вид с информацией
     */
    @GetMapping("/accidents/{accidentId}")
    public ModelAndView show(@PathVariable("accidentId") int id) {
        return new ModelAndView("accident/show-accident")
                    .addObject("accident", accidentService.findById(id));
    }

    /**
     * Возвращает вид с информацией выбранного автоинцидента и предложением
     * пользователю выполнить удаление этого автоинцидента
     * @param id идентификатор автоинцидента
     * @return возвращает вид с информацией и предложением
     * пользователю выполнить удаление автоинцидента
     */
    @GetMapping("/accidents/{accidentId}/delete/confirm")
    public ModelAndView viewDeleteAccident(@PathVariable("accidentId") int id) {
        return new ModelAndView("accident/show-accident")
                .addObject("accident", accidentService.findById(id))
                .addObject("deleteConfirm", true);
    }

    /**
     * Удаляет автоинцидент из хранилища по id
     * @param id идентификатор автоинцидента
     * @return вид с информацией об успешном удалении автоинцидента
     * @throws NoSuchElementException - если автоинцидент не найден
     * в хранилище
     */
    @GetMapping("/accidents/{accidentId}/delete")
    public ModelAndView delete(@PathVariable("accidentId") int id) {
        Accident accident = accidentService.findById(id);
        accidentService.delete(accident);
        System.out.println("Accident deleted successfully");
        return new ModelAndView("accident/delete-accident-success")
                    .addObject("accident", accident);
    }
}
