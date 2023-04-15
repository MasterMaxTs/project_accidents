package ru.job4j.accidents.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Document;
import ru.job4j.accidents.model.TrackingStates;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.document.DocumentService;
import ru.job4j.accidents.service.rule.RuleService;
import ru.job4j.accidents.service.status.StatusService;
import ru.job4j.accidents.service.type.AccidentTypeService;
import ru.job4j.accidents.service.user.UserService;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
    private final DocumentService documentService;

    /**
     * Конструктор
     * @param accidentService     сервис автоинцидентов
     * @param accidentTypeService сервис типов автоинцидентов
     * @param ruleService         сервис статей автонарушений
     * @param statusService       сервис статусов сопровождения автоинцидентов
     * @param userService         сервис пользователей
     * @param documentService     сервис сопроводительных документов
     */
    public AccidentController(@Qualifier("accidentDataService")
                              AccidentService accidentService,
                              @Qualifier("accidentTypeDataService")
                              AccidentTypeService accidentTypeService,
                              @Qualifier("ruleDataService")
                              RuleService ruleService,
                              @Qualifier("statusDataService")
                              StatusService statusService,
                              UserService userService,
                              DocumentService documentService) {
        this.accidentService = accidentService;
        this.accidentTypeService = accidentTypeService;
        this.ruleService = ruleService;
        this.statusService = statusService;
        this.userService = userService;
        this.documentService = documentService;
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
                        id, TrackingStates.ACCEPTED_STATUS.getId());
        return accident == null
                ? new ModelAndView("user/error/edit-accident-prohibition")
                     .addObject("id", id)
                : new ModelAndView("user/accident/edit-accident")
                     .addObject("accident", accident)
                     .addObject("types", accidentTypeService.findAll())
                     .addObject("documents", documentService.findAllByAccidentId(id));
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
                    .addObject("documents", documentService.findAllByAccidentId(accidentId))
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
    @PostMapping(value = "/saveAccident", consumes = MULTIPART_FORM_DATA_VALUE)
    public ModelAndView save(@ModelAttribute Accident accident,
                             @RequestParam("type.id") int typeId,
                             @RequestParam("username") String userName,
                             @RequestParam("photo") MultipartFile[] files) {
        accident.setType(accidentTypeService.findById(typeId));
        accident.setCreated(LocalDateTime.now());
        accident.setUser(userService.findByUserName(userName));
        accident.setStatus(
                statusService.findById(TrackingStates.ACCEPTED_STATUS.getId())
        );
        documentService.saveAccidentDocumentsFromRequest(files,
                                accidentService.add(accident), getUsername());
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
     * @param rIds строковый массив из идентификаторов статей автонарушений
     * @return вид по имени user/accident/edit-accident-success
     */
    @PostMapping(value = "/updateAccident", consumes = MULTIPART_FORM_DATA_VALUE)
    public ModelAndView update(@ModelAttribute Accident accident,
                               @RequestParam("type.id") int typeId,
                               @RequestParam("username") String username,
                               @RequestParam("status.id") int statusId,
                               @RequestParam(value = "photo", required = false) MultipartFile[] files,
                               @RequestParam(value = "rIds", required = false) String[] rIds) {
        accident.setType(accidentTypeService.findById(typeId));
        accident.setRules(ruleService.getRulesFromIds(rIds));
        accident.setUpdated(LocalDateTime.now());
        accident.setStatus(statusService.findById(statusId));
        accident.setUser(userService.findByUserName(username));
        accidentService.update(accident);
        documentService.saveAccidentDocumentsFromRequest(files, accident, getUsername());
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
                    .addObject("accident", accidentService.findById(id))
                    .addObject("documents", documentService.findAllByAccidentId(id));
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
        documentService.deleteAllByAccidentId(id);
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
        documentService.deleteAllByStatusId(TrackingStates.ARCHIVED_STATUS.getId());
        accidentService.deleteAllArchived();
        System.out.println("Car incidents archive successfully cleared!");
        return new ModelAndView("admin/accident/delete-all-from-archive-success");
    }

    /**
     * Возвращает вид для отображения сопроводительного документа пользователю
     * @param documentId идентификатор документа
     * @return вид по имени user/accident/show-accident-document с данными
     */
    @GetMapping("documents/{documentId}/show")
    public ModelAndView viewShowDocument(@PathVariable("documentId") int documentId) {
        return new ModelAndView("user/accident/show-accident-document")
                .addObject("document", documentService.findById(documentId));
    }

    /**
     * Загружает на страницу вида автоинцидента изображение
     * сопроводительного документа
     * @param id идентификатор документа
     * @return ResponseEntity
     */
    @GetMapping("/documents/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable("documentId") int id) {
        Document documentInDb = documentService.findById(id);
            return ResponseEntity.ok()
                    .headers(new HttpHeaders())
                    .contentLength(documentInDb.getData().length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayResource(documentInDb.getData()));
    }

    /**
     * Возвращает вид-предупреждение пользователю об удалении
     * сопроводительного документа из автоинцидента
     * @param documentId идентификатор документа
     * @return вид по имени "user/accident/delete-accident-document-confirm"
     */
    @GetMapping("/documents/{documentId}/delete/confirm")
    public ModelAndView viewDeleteDocumentConfirm(@PathVariable("documentId") int documentId) {
        return new ModelAndView("user/accident/delete-accident-document-confirm")
                .addObject("document", documentService.findById(documentId));
    }

    /**
     * Удаляет сопроводительный документ из хранилища по id документа
     * @param documentId идентификатор сопроводительного документа
     * @return вид по имени user/accident/delete-accident-document-success.html
     */
    @GetMapping("/documents/{documentId}/delete")
    public ModelAndView deleteDocument(@PathVariable("documentId") int documentId) {
        Document documentInDb = documentService.findById(documentId);
        documentService.delete(documentInDb);
        return new ModelAndView("user/accident/delete-accident-document-success.html")
                .addObject("document", documentInDb);
    }
}
