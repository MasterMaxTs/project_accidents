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
     * Сохраняет или обновляет автоинцидент в хранилище
     * @param accident автоинцидент
     * @param flag флаг, указывающий какое действие совершается с автоинцидентом
     * @return перенаправление на страницу со всеми автоинцидентами
     */
    @PostMapping("/saveAccident")
    public String saveOrUpdate(@ModelAttribute Accident accident,
                               @RequestParam(value = "update", required = false) String flag) {
        String rsl;
        if (flag == null) {
            service.add(accident);
            System.out.println("Accident created successfully");
            rsl = "redirect:/index";
        } else {
            service.update(accident);
            System.out.println("Accident updated successfully");
            rsl = "accident/update-accident-confirm";
        }
        return rsl;
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
