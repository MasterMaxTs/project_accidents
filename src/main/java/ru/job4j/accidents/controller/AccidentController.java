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
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("stubType", "---");
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
            accident = accidentService.findById(id);
        } catch (NoSuchElementException nse) {
            return new ModelAndView("error/404")
                    .addObject("errorMsg", nse.getMessage());
        }
        return new ModelAndView("accident/create-accident")
                    .addObject("accident", accident)
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("update", true);
    }

    /**
     * Сохраняет автоинцидент в хранилище
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @return перенаправление на страницу со всеми инцидентами, либо
     * возвращает вид с информированием пользователя, что в процессе выполнения
     * операции произошла ошибка
     */
    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident,
                       @RequestParam("type.id") int typeId) {
        String rsl = setAccidentType(accident, typeId);
        if (rsl.equals("")) {
            accidentService.add(accident);
            System.out.println("Accident created successfully");
            rsl = "redirect:/index";
        }
        return rsl;
    }

    /**
     * Обновляет данные автоинцидента в хранилище
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @return вид с информацией об успешном обновлении данных, либо
     * возвращает вид с информированием пользователя, что в процессе выполнения
     * операции произошла ошибка
     */
    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident,
                         @RequestParam("type.id") int typeId) {
        String rsl = setAccidentType(accident, typeId);
        if (rsl.equals("")) {
            accidentService.update(accident);
            System.out.println("Accident updated successfully");
            rsl = "accident/update-accident-success";
        }
        return rsl;
    }

    /**
     * Устанавливает значение полю type автоинцидента
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @return пустую строку, если операция прошла успешно, либо
     * перенаправление на страницу с информированием пользователя об ошибке
     * @exception NoSuchElementException в случае, если тип автоинцидента не
     * найден в хранилище типов автоинцидентов
     */
    private String setAccidentType(Accident accident, int typeId) {
        String rsl = "";
        try {
            accident.setType(accidentTypeService.findById(typeId));
        } catch (NoSuchElementException nse) {
            return String.format(
                    "redirect:/error/404?message=%s", nse.getMessage()
            );
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
            accident = accidentService.findById(id);
            accidentService.delete(accident);
            System.out.println("Delete success");
        } catch (NoSuchElementException nse) {
            return String.format(
                    "redirect:/error/404?message=%s", nse.getMessage());
        }
        return "redirect:/index";
    }
}
