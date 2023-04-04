package ru.job4j.accidents.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.status.StatusRepository;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.rule.RuleService;
import ru.job4j.accidents.service.status.StatusService;
import ru.job4j.accidents.service.type.AccidentTypeService;
import ru.job4j.accidents.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Контроллер автоинцидентов
 */
@Controller
public class AccidentController {

    /**
     * Ссылки на слои сервисов
     */
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;
    private final RuleService ruleService;
    private final StatusService statusService;
    private final UserService userService;

    /**
     * Конструктор
     * @param accidentService     сервис автоинцидентов
     * @param accidentTypeService сервис типов автоинцидентов
     * @param ruleService         сервис статей автонарушений
     * @param statusService       сервис статусов сопровождения автоинцидентов
     * @param userService         сервис пользователей
     */
    public AccidentController(@Qualifier("accidentDataService")
                              AccidentService accidentService,
                              @Qualifier("accidentTypeDataService")
                              AccidentTypeService accidentTypeService,
                              @Qualifier("ruleDataService")
                              RuleService ruleService,
                              @Qualifier("statusDataService")
                              StatusService statusService,
                              UserService userService) {
        this.accidentService = accidentService;
        this.accidentTypeService = accidentTypeService;
        this.ruleService = ruleService;
        this.statusService = statusService;
        this.userService = userService;
    }

    /**
     * Добавляет имя пользователя, авторизовавшегося в приложении,
     * во все модели контроллера
     * @return имя авторизовавшегося в приложении пользователя
     */
    @ModelAttribute("username")
    public String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                                                .getContext()
                                                .getAuthentication()
                                                .getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * Возращает вид с заполненным списком всех автоинцидентов
     * @return вид по имени "admin/index/index"
     * с заполненным списком всех автоинцидентов
     */
    @GetMapping("/getAllAccidents")
    public ModelAndView getAll() {
        return new ModelAndView("admin/index/index")
                    .addObject("accidents", accidentService.findAll());
    }

    /**
     * Возращает вид с заполненным списком всех автоинцидентов, относящихся к
     * конкретному пользователю
     * @return вид по имени "user/index/index"
     * с заполненным списком всех автоинцидентов пользователя
     */
    @GetMapping("/getAllByUser")
    public ModelAndView getAllByUser(Model model) {
        String userName = (String) model.getAttribute("username");
        return new ModelAndView("user/index/index")
            .addObject("accidents", accidentService.findAllByUserName(userName));
    }

    /**
     * Возращает вид с заполненным списком всех автоинцидентов, имеющих
     * статус сопровождения 'Ожидание'
     * @return вид по имени /admin/accident/all-queued-accidents"
     * с заполненным списком всех автоинцидентов, имеющих
     * статус сопровождения 'Ожидание'
     */
    @GetMapping("/allQueued")
    public ModelAndView getAllQueued() {
        return new ModelAndView("admin/accident/all-queued-accidents")
            .addObject("accidents", accidentService.findAllQueued());
    }

    /**
     * Возращает вид с заполненным списком всех автоинцидентов, имеющих
     * статус сопровождения 'Архив'
     * @return вид по имени /admin/accident/all-archived-accidents"
     * с заполненным списком всех автоинцидентов,имеющих
     * статус сопровождения 'Архив'
     */
    @GetMapping("/allArchived")
    public ModelAndView getAllArchived() {
        return new ModelAndView("admin/accident/all-archived-accidents")
            .addObject("accidents", accidentService.findAllArchived());
    }

    /**
     * Возращает вид для создания нового автоинцидента
     * @return вид по имени "user/accident/create-accident"
     * в виде формы с заполненным полем выбора 'Тип'
     */
    @GetMapping("/createAccident")
    public ModelAndView viewCreateAccident(@ModelAttribute Accident accident) {
        return new ModelAndView("user/accident/create-accident")
                    .addObject("types", accidentTypeService.findAll());
    }

    /**
     * Возвращает вид для редактирования автоинцидента, либо вид, запрещающий
     * его редактирование, в зависимости от проверки текущего статуса
     * автоинцидента
     * @param id идентификатор автоинцидента
     * @return вид по имени /user/accident/edit-accident в виде формы с
     * заполненным полем выбора 'Тип',
     * либо вид по имени user/error/edit-accident-prohibition в виде
     * сообщения об ошибке редактирования
     */
    @GetMapping("accidents/{accidentId}/check_status")
    public ModelAndView viewUpdateAccidentByUserAsResultOfStatusCheck(
                                        @PathVariable("accidentId") int id) {
        Accident accident =
                accidentService.checkAccidentForStatus(
                        id, StatusRepository.ACCEPTED_STATUS_ID);
        return accident == null
                ? new ModelAndView("user/error/edit-accident-prohibition")
                     .addObject("id", id)
                : new ModelAndView("user/accident/edit-accident")
                     .addObject("accident", accident)
                     .addObject("types", accidentTypeService.findAll());
    }

    /**
     * Обновляет статус сопровождения автоинцидента на 'Ожидание',
     * возвращает сообщение пользователю (ROLE_ADMIN)
     * @param accidentId идентификатор автоинцидента
     * @param statusId идентификатор статуса сопровождения автоинцидента
     * @return вид по имени "admin/accident/add-to-queue-success"
     */
    @PostMapping("/accidents/{accidentId}/addToQueue")
    public ModelAndView addAccidentToQueue(
                                    @PathVariable("accidentId") int accidentId,
                                    @RequestParam("status.id") int statusId) {
        Accident accident = accidentService.findById(accidentId);
        accident.setStatus(statusService.findById(statusId));
        accidentService.update(accident);
        return new ModelAndView("admin/accident/add-to-queue-success")
                .addObject("accident", accident);
    }

    /**
     * Обновляет статус сопровождения автоинцидента на 'Архив',
     * возвращает сообщение пользователю (ROLE_ADMIN)
     * @param accidentId идентификатор автоинцидента
     * @param statusId идентификатор статуса сопровождения автоинцидента
     * @return вид по имени "admin/accident/add-to-archive-success"
     */
    @PostMapping("/accidents/{accidentId}/addToArchive")
    public ModelAndView addAccidentToArchive(
                                    @PathVariable("accidentId") int accidentId,
                                    @RequestParam("status.id") int statusId) {
        Accident accident = accidentService.findById(accidentId);
        accident.setStatus(statusService.findById(statusId));
        accidentService.update(accident);
        return new ModelAndView("admin/accident/add-to-archive-success")
                .addObject("accident", accident);
    }

    /**
     * Возвращает вид-предупреждение пользователю (ROLE_ADMIN) о добавлении
     * автоинцидента в очередь на обработку
     * @param id идентификатор автоинцидента
     * @return вид по имени "admin/accident/add-to-queue-confirm" с полной
     * информацией об автоинциденте
     */
    @GetMapping("/accidents/{accidentId}/addToQueue/confirm")
    public ModelAndView viewAddAccidentToQueueConfirm(
                                        @PathVariable("accidentId") int id) {
        return new ModelAndView("admin/accident/add-to-queue-confirm")
                .addObject("accident", accidentService.findById(id));
    }

    /**
     * Возвращает вид-предупреждение пользователю (ROLE_ADMIN) о добавлении
     * автоинцидента в архив
     * @param id идентификатор автоинцидента
     * @return вид по имени "admin/accident/add-to-archive-confirm" с полной
     * информацией об автоинциденте
     */
    @GetMapping("/accidents/{accidentId}/addToArchive/confirm")
    public ModelAndView viewAddAccidentToArchiveConfirm(
                                       @PathVariable("accidentId") int id) {
        return new ModelAndView("admin/accident/add-to-archive-confirm")
                .addObject("accident", accidentService.findById(id));
    }

    /**
     * Возвращает вид-предупреждение пользователю (ROLE_ADMIN) об удалении
     * автоинцидента из архива
     * @param id идентификатор автоинцидента
     * @return вид по имени "admin/accident/delete-from-archive-confirm" с полной
     * информацией об автоинциденте
     */
    @GetMapping("/accidents/{accidentId}/delete/confirm")
    public ModelAndView viewDeleteAccidentConfirm(
                                        @PathVariable("accidentId") int id) {
        return new ModelAndView("admin/accident/delete-from-archive-confirm")
                .addObject("accident", accidentService.findById(id));
    }

    /**
     * Возвращает вид-предупреждение пользователю (ROLE_ADMIN) об удалении
     * всех автоинцидентов из архива
     * @return вид по имени "admin/accident/delete-all-from-archive-confirm"
     */
    @GetMapping("/deleteAllArchived/confirm")
    public ModelAndView viewDeleteAllArchivedConfirm() {
        return new ModelAndView("admin/accident/delete-all-from-archive-confirm");
    }

    /**
     * Обновляет статус сопровождения автоинцидента на 'Рассматривается',
     * возращает вид для редактирования автоинцидента пользователем (ROLE_ADMIN)
     * @return вид по имени "admin/accident/edit-accident" в виде формы с
     * заполненным полем выбора 'Тип' и полем выбора 'Статьи'
     */
    @GetMapping("/accidents/{accidentId}/edit")
    public ModelAndView viewUpdateAccidentByAdmin(
                                    @PathVariable("accidentId") int accidentId,
                                    @RequestParam("status.id") int statusId) {
        Accident accident = accidentService.findById(accidentId);
        accident.setStatus(statusService.findById(statusId));
        accidentService.update(accident);
        return new ModelAndView("admin/accident/edit-accident")
                    .addObject("accident", accident)
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("rules", ruleService.findAll());
    }

    /**
     * Сохраняет автоинцидент в хранилище,
     * возвращает сообщение пользователю
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @param userName имя пользователя
     * @return вид по имени user/accident/create-accident-success
     */
    @PostMapping("/saveAccident")
    public ModelAndView save(@ModelAttribute Accident accident,
                             @RequestParam("type.id") int typeId,
                             @RequestParam("username") String userName) {
        accident.setType(accidentTypeService.findById(typeId));
        accident.setCreated(LocalDateTime.now());
        accident.setUser(userService.findByUserName(userName));
        accident.setStatus(
                statusService.findById(StatusRepository.ACCEPTED_STATUS_ID)
        );
        accidentService.add(accident);
        System.out.println("Accident created successfully");
        return new ModelAndView("user/accident/create-accident-success");
    }

    /**
     * Обновляет данные об автоинциденте в хранилище
     * возвращает сообщение пользователю
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @param username имя пользователя
     * @param statusId статус сопровождения автоинцидента
     * @param request HttpServletRequest
     * @return вид по имени user/accident/edit-accident-success
     */
    @PostMapping("/updateAccident")
    public ModelAndView update(@ModelAttribute Accident accident,
                               @RequestParam("type.id") int typeId,
                               @RequestParam("username") String username,
                               @RequestParam("status.id") int statusId,
                               HttpServletRequest request) {
        accident.setType(accidentTypeService.findById(typeId));
        accident.setRules(ruleService.getRulesFromIds(request, "rIds"));
        accident.setUpdated(LocalDateTime.now());
        accident.setStatus(statusService.findById(statusId));
        accident.setUser(userService.findByUserName(username));
        accidentService.update(accident);
        System.out.println("Accident updated successfully");
        return new ModelAndView("user/accident/edit-accident-success");
    }

    /**
     * Возвращает вид с подробным описанием автоинцидента
     * @param id идентификатор автоинцидента
     * @return возвращает вид по имени user/accident/show-accident
     */
    @GetMapping("/accidents/{accidentId}")
    public ModelAndView show(@PathVariable("accidentId") int id) {
        return new ModelAndView("user/accident/show-accident")
                    .addObject("accident", accidentService.findById(id));
    }

    /**
     * Удаляет автоинцидент из хранилища по id,
     * возвращает сообщение пользователю (ROLE_ADMIN)
     * @param id идентификатор автоинцидента
     * @return вид по имени admin/accident/delete-from-archive-success
     */
    @GetMapping("/accidents/{accidentId}/delete")
    public ModelAndView delete(@PathVariable("accidentId") int id) {
        Accident accident = accidentService.findById(id);
        accidentService.delete(accident);
        System.out.println("Accident deleted successfully");
        return new ModelAndView("admin/accident/delete-from-archive-success")
                    .addObject("accident", accident);
    }

    /**
     * Удаляет все автоинциденты из хранилища, имеющие статус 'Архив',
     * возвращает сообщение пользователю (ROLE_ADMIN)
     * @return вид по имени admin/accident/delete-all-from-archive-success
     */
    @GetMapping("/deleteAllArchived")
    public ModelAndView deleteAllArchived() {
        accidentService.deleteAllArchived();
        System.out.println("Car incidents archive successfully cleared!");
        return new ModelAndView("admin/accident/delete-all-from-archive-success");
    }
}
