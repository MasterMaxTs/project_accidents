package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.accident.AccidentService;

import java.util.NoSuchElementException;

/**
 * Контроллер автонарушений
 */
@Controller
@AllArgsConstructor
public class AccidentController {

    /**
     * Ссылка на слой сервисов
     */
    private AccidentService service;

    @ModelAttribute("user")
    public String getCurrentUserName() {
        return "Maxim Tsurkanov";
    }

    /**
     * Возращает вид с данными в виде всех автоинцидентов
     * @return вид по имени "index" со всеми нарушениями "accidents"
     * в виде списка
     */
    @GetMapping("/getAllAccidents")
    public ModelAndView getAll() {
        return new ModelAndView("index")
                        .addObject("accidents", service.findAll());
    }

    /**
     * Возращает вид для создания нового автоинцидента
     * @return вид по имени "accident/create-accident"
     * в виде формы
     */
    @GetMapping("/createAccident")
    public ModelAndView viewCreateAccident(@ModelAttribute Accident accident) {
        return new ModelAndView("accident/create-accident");
    }

    /**
     * Возращает вид для обновления сведений об автоинциденте
     * @return вид по имени "accident/create-accident" с заполненными полями
     * формы начальными данными
     * @exception NoSuchElementException в случае, если автоинцидент не найден
     * в хранилище, возвращает вид по имени "error/404" с информацией об ошибке
     */
    @GetMapping("/accidents/{accidentId}/edit")
    public ModelAndView viewUpdateAccident(@PathVariable("accidentId") int id) {
        Accident accident;
        try {
            accident = service.findById(id);
        } catch (NoSuchElementException nse) {
            return new ModelAndView("error/404")
                    .addObject("errorMsg", nse.getMessage());
        }
        return new ModelAndView("accident/create-accident")
                    .addObject("accident", accident)
                    .addObject("update", true);
    }

    /**
     * Сохраняет автоинцидент в хранилище
     * @param accident автоинцидент
     * @return перенаправление на страницу со всеми инцидентами
     */
    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident) {
        service.add(accident);
        System.out.println("Accident created successfully");
        return "redirect:/index";
    }

    /**
     * Обновляет данные автоинцидента в хранилище
     * @param accident автоинцидент
     * @return вид с информацией об успешном обновлении данных
     */
    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident) {
        service.update(accident);
        System.out.println("Accident updated successfully");
            return "accident/update-accident-success";
    }

    /**
     * Удаляет автоинцидент из хранилища по id
     * @param id идентификатор автоинцидента
     * @exception NoSuchElementException в случае, если автоинцидент не найден
     * в хранилище, возвращает вид по имени "error/404" с информацией об ошибке
     * @return перенаправление на страницу со всеми автоинцидентами
     */
    @GetMapping("/accidents/{accidentId}/delete")
    public String delete(@PathVariable("accidentId") int id) {
        Accident accident;
        try {
            accident = service.findById(id);
            service.delete(accident);
            System.out.println("Delete success");
        } catch (NoSuchElementException nse) {
            return String.format(
                    "redirect:/error/404?message=%s", nse.getMessage());
        }
        return "redirect:/index";
    }
}
