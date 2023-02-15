package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.accident.type.AccidentTypeService;

import java.util.NoSuchElementException;

/**
 * Контроллер автонарушений
 */
@Controller
@AllArgsConstructor
public class AccidentController {

    /**
     * Ссылки на слои сервисов
     */
    private AccidentService accidentService;
    private AccidentTypeService accidentTypeService;

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
                        .addObject("accidents", accidentService.findAll());
    }

    /**
     * Возращает вид для создания нового автоинцидента
     * @return вид по имени "accident/create-accident"
     * в виде формы
     */
    @GetMapping("/createAccident")
    public ModelAndView viewCreateAccident(@ModelAttribute Accident accident) {
        return new ModelAndView("accident/create-accident")
                    .addObject("types", accidentTypeService.findAll());
    }

    /**
     * Возращает вид для обновления сведений об автоинциденте
     * @return вид по имени "accident/create-accident" с заполненными полями
     * формы начальными данными
     * @exception NoSuchElementException - если автоинцидент
     * не найден в хранилище
     */
    @GetMapping("/accidents/{accidentId}/edit")
    public ModelAndView viewUpdateAccident(@PathVariable("accidentId") int id) {
        return new ModelAndView("accident/create-accident")
                    .addObject("accident", accidentService.findById(id))
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("update", true);
    }

    /**
     * Сохраняет автоинцидент в хранилище
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @return перенаправление на страницу со всеми инцидентами
     * @exception NoSuchElementException - если тип автоиницидента
     * не найден в хранилище
     */
    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident,
                       @RequestParam("type.id") int typeId) {
        accident.setType(accidentTypeService.findById(typeId));
        accidentService.add(accident);
        System.out.println("Accident created successfully");
        return "redirect:/index";
    }

    /**
     * Обновляет данные автоинцидента в хранилище
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @return вид с информацией об успешном обновлении данных
     * @exception NoSuchElementException - если тип автоиницидента
     * не найден в хранилище
     *
     */
    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident,
                         @RequestParam("type.id") int typeId) {
        accident.setType(accidentTypeService.findById(typeId));
        accidentService.update(accident);
        System.out.println("Accident updated successfully");
        return "accident/update-accident-success";
    }

    /**
     * Удаляет автоинцидент из хранилища по id
     * @param id идентификатор автоинцидента
     * @return перенаправление на страницу со всеми автоинцидентами
     * @exception NoSuchElementException - если автоинцидент не найден
     * в хранилище
     */
    @GetMapping("/accidents/{accidentId}/delete")
    public String delete(@PathVariable("accidentId") int id) {
        accidentService.delete(accidentService.findById(id));
        System.out.println("Accident deleted successfully");
        return "redirect:/index";
    }
}
