package ru.job4j.accidents.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.accidents.model.*;
import ru.job4j.accidents.service.accident.AccidentService;
import ru.job4j.accidents.service.card.RegistrationCardService;
import ru.job4j.accidents.service.document.DocumentService;
import ru.job4j.accidents.service.rule.RuleService;
import ru.job4j.accidents.service.status.StatusService;
import ru.job4j.accidents.service.type.AccidentTypeService;
import ru.job4j.accidents.service.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * Контроллер автоинцидентов
 */
@Controller
@Slf4j
public class AccidentController {

    /**
     * Ссылки на слои сервисов
     */
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;
    private final RuleService ruleService;
    private final StatusService statusService;
    private final UserService userService;
    private final RegistrationCardService registrationCardService;
    private final DocumentService documentService;

    /**
     * Конструктор
     * @param accidentService     сервис автоинцидентов
     * @param accidentTypeService сервис типов автоинцидентов
     * @param ruleService         сервис статей автонарушений
     * @param statusService       сервис статусов сопровождения автоинцидентов
     * @param userService         сервис пользователей
     * @param registrationCardService сервис учёта автомобилей в ГИБДД
     * @param documentService     сервис сопроводительных документов
     */
    public AccidentController(@Qualifier("accidentServiceImpl")
                              AccidentService accidentService,
                              AccidentTypeService accidentTypeService,
                              RuleService ruleService,
                              StatusService statusService,
                              UserService userService,
                              RegistrationCardService registrationCardService,
                              @Qualifier("documentDataService")
                              DocumentService documentService) {
        this.accidentService = accidentService;
        this.accidentTypeService = accidentTypeService;
        this.ruleService = ruleService;
        this.statusService = statusService;
        this.userService = userService;
        this.registrationCardService = registrationCardService;
        this.documentService = documentService;
    }

    /**
     * Добавляет имя пользователя, авторизовавшегося в приложении,
     * во все модели контроллера
     * @return имя авторизовавшегося в приложении пользователя
     */
    @ModelAttribute("username")
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
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
     * Возращает вид со списком всех автоинцидентов, относящихся к
     * конкретному пользователю
     * @return вид по имени "user/index/index"
     * с заполненным списком всех автоинцидентов пользователя
     */
    @GetMapping("/getAllByUser")
    public ModelAndView getAllByUser() {
        return new ModelAndView("user/index/index")
            .addObject("accidents",
                                 accidentService.findAllByUserName(getUsername())
            );
    }

    /**
     * Возращает вид со списком всех автоинцидентов, найденных в
     * базе данных по регистрационному знаку автомобиля
     * @param carPlate регистрационный знак автомобиля
     * @return вид по имени "admin/accident/all-filtered-accidents",
     * если автоинциденты найдены, иначе вид "admin/accident/no-filtered-accidents-inform"
     * для информирования пользователя
     */
    @GetMapping("/getAllByCarPlate")
    public ModelAndView getAllByCarPlate(@RequestParam("car.plate") String carPlate) {
        List<Accident> accidents = accidentService.findAllByCarPlate(carPlate);
        String message = String.format(
                        "Найденные инциденты по регистрационному знаку ТС [%s]", carPlate
        );
        String errorMessage = String.format(
                "По запрошенному ГРЗ ТС [%s]"
                + " автоинцидентов в базе данных не нашлось!"
                + " Уточните значение ГРЗ ТС собственника!", carPlate
        );
        return accidents.isEmpty()
                ? new ModelAndView("admin/accident/no-filtered-accidents-inform")
                    .addObject("errorMessage", errorMessage)
                : new ModelAndView("admin/accident/all-filtered-accidents")
                    .addObject("accidents", accidents)
                    .addObject("filterName", message)
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("statuses", statusService.findAll());
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
     * статус сопровождения 'Возвращённый'
     * @return вид по имени /admin/accident/all-queued-accidents"
     * с заполненным списком всех автоинцидентов, имеющих
     * статус сопровождения 'Ожидание'
     */
    @GetMapping("/allReturned")
    public ModelAndView getAllReturned() {
        return new ModelAndView("admin/accident/all-returned-accidents")
                .addObject("accidents", accidentService.findAllReturned());
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
     * @param accident автоинцидент
     * @return вид по имени "user/accident/create-accident"
     * в виде формы с заполненным полем выбора 'Тип'
     */
    @GetMapping("/createAccident")
    public ModelAndView viewCreateAccident(@ModelAttribute Accident accident) {
        return new ModelAndView("user/accident/create-accident")
                    .addObject("types", accidentTypeService.findAll())
                    .addObject("registrationCards",
                            userService.findByUserName(getUsername()).getRegistrationCards()
                    );
    }

    /**
     * Возвращает вид для редактирования автоинцидента пользователем
     * ROLE_USER
     * @param id идентификатор автоинцидента
     * @param userName username инициатора автоинцидента
     * @param notice уведомление инициатору автоинцидента
     * @return вид по имени /user/accident/edit-accident в виде формы с данными
     */
    @GetMapping("accidents/{accidentId}/edit_by_user")
    public ModelAndView viewUpdateAccidentByUser(
                                    @PathVariable("accidentId") int id,
                                    @RequestParam("username") String userName,
                                    @RequestParam(value = "notice",
                                            required = false) String notice) {
        return new ModelAndView("user/accident/edit-accident")
                .addObject("accident", accidentService.findById(id))
                .addObject("registrationCars",
                        userService.findByUserName(userName).getRegistrationCards())
                .addObject("types", accidentTypeService.findAll())
                .addObject("documents", documentService.findAllByAccidentId(id))
                .addObject("notice", notice);
    }

    /**
     * Обновляет статус сопровождения автоинцидента на 'Ожидание',
     * возвращает сообщение пользователю (ROLE_ADMIN)
     * @param accidentId идентификатор автоинцидента
     * @param notice уведомление инициатору автоинцидента
     * @return вид по имени "admin/accident/add-to-queue-success" с
     * автоинцидентом
     */
    @PostMapping("/accidents/{accidentId}/addToQueue")
    public ModelAndView addAccidentToQueue(
                @RequestParam(value = "notice", required = false) String notice,
                @PathVariable("accidentId") int accidentId) {
        Accident accident = accidentService.findById(accidentId);
        accident.setStatus(statusService.findById(TrackingStates.QUEUED_STATUS.getId()));
        accident.setNotice(notice);
        accidentService.update(accident);
        return new ModelAndView("admin/accident/add-to-queue-success")
                     .addObject("accident", accident);
    }

    /**
     * Обновляет статус сопровождения автоинцидента на 'Архив',
     * возвращает сообщение пользователю (ROLE_ADMIN)
     * @param accidentId идентификатор автоинцидента
     * @return вид по имени "admin/accident/add-to-archive-success"
     * с автоинцидентом
     */
    @PostMapping("/accidents/{accidentId}/addToArchive")
    public ModelAndView addAccidentToArchive(
                                    @PathVariable("accidentId") int accidentId) {
        Accident accident = accidentService.findById(accidentId);
        accident.setStatus(statusService.findById(TrackingStates.ARCHIVED_STATUS.getId()));
        accidentService.update(accident);
        return new ModelAndView("admin/accident/add-to-archive-success")
                .addObject("accident", accident);
    }

    /**
     * Делает возврат заявки, оформленной как автоинцидент, инициатору
     * @param notice уведомление инцициатору
     * @param accidentId идентификатор автоинцидента
     * @return вид по имени "admin/accident/return-to-user-success"
     * с автоинцидентом
     */
    @PostMapping("/accidents/{accidentId}/return")
    public ModelAndView returnAccidentToUser(@RequestParam("notice") String notice,
                                             @PathVariable("accidentId") int accidentId) {
        Accident accident = accidentService.findById(accidentId);
        accident.setStatus(statusService.findById(TrackingStates.RETURNED_STATUS.getId()));
        accident.setNotice(notice);
        accidentService.update(accident);
        return new ModelAndView("admin/accident/return-to-user-success")
                .addObject("accident", accident);
    }

    /**
     * Возвращает вид-предупреждение пользователю (ROLE_ADMIN) о добавлении
     * автоинцидента в очередь на обработку
     * @param id идентификатор автоинцидента
     * @return вид по имени "admin/accident/add-to-queue-confirm" с
     * информацией об автоинциденте и приложенных к нему сопроводительным
     * документам
     */
    @GetMapping("/accidents/{accidentId}/addToQueue/confirm")
    public ModelAndView viewAddAccidentToQueueConfirm(
                                        @PathVariable("accidentId") int id) {
        return new ModelAndView("admin/accident/add-to-queue-confirm")
                    .addObject("accident", accidentService.findById(id))
                    .addObject("documents",
                            documentService.findAllByAccidentId(id));
    }

    /**
     * Возвращает вид пользователю (ROLE_ADMIN) с возможностью внести
     * уведомление инициатору автоинцидента перед добавлением автоинцидента
     * в очередь на обработку
     * @param id идентификатор автоинцидента
     * @return вид по имени admin/accident/add-to-queue-notice с данными в виде
     * идентификатора автоинцидента
     */
    @GetMapping("accidents/{accidentId}/addToQueue/notice")
    public ModelAndView viewAddAccidentToQueueAppendNotice(
                                         @PathVariable("accidentId") int id) {
        return new ModelAndView("admin/accident/add-to-queue-notice")
                     .addObject("id", id);
    }

    /**
     * Возвращает вид пользователю (ROLE_ADMIN) для подтверждения действия
     * возврата заявки (автоинцидента) инициатору
     * @param id идентификатор автоинцидента
     * @return вид по имени admin/accident/return-to-user-confirm
     * с информацией об автоинциденте и приложенных к нему сопроводительным
     * документам
     */
    @GetMapping("/accidents/{accidentId}/return/confirm")
    public ModelAndView viewReturnAccidentToUserConfirm(
                                         @PathVariable("accidentId") int id) {
        return new ModelAndView("admin/accident/return-to-user-confirm")
                .addObject("accident", accidentService.findById(id))
                .addObject("documents",
                        documentService.findAllByAccidentId(id));
    }

    /**
     * Возвращает вид пользователю (ROLE_ADMIN) с возможностью внести
     * уведомление инициатору автоинцидента перед возвратом заявки
     * (автоинцидента)
     * @param id идентификатор автоинцидента
     * @return вид по имени admin/accident/return-to-user-notice с данными в виде
     * идентификатора автоинцидента
     */
    @GetMapping("/accidents/{accidentId}/return/notice")
    public ModelAndView viewReturnAccidentToUser(
                                         @PathVariable("accidentId") int id) {
        return new ModelAndView("admin/accident/return-to-user-notice")
                     .addObject("id", id);
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
        log.info("Attempt by the administrator to delete the accident(id={}) "
                    + "from the accident archive", id);
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
        log.info("Attempt by the administrator to perform a full cleanup of "
                     + "the accident archive!");
        return new ModelAndView("admin/accident/delete-all-from-archive-confirm");
    }

    /**
     * Обновляет статус сопровождения автоинцидента на 'Рассматривается',
     * возращает вид для редактирования автоинцидента пользователем (ROLE_ADMIN)
     * @param accidentId идентификатор автоинцидента
     * @return вид по имени "admin/accident/edit-accident" в виде формы с
     * заполненным полем выбора 'Тип' и полем выбора 'Статьи'
     */
    @GetMapping("/accidents/{accidentId}/edit")
    public ModelAndView viewUpdateAccidentByAdmin(
                                    @PathVariable("accidentId") int accidentId) {
        Accident accident = accidentService.findById(accidentId);
        accident.setStatus(statusService.findById(TrackingStates.IN_WORKED_STATUS.getId()));
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
     * @param carPlate ГРЗ автомобиля
     * @param typeId идентификатор типа автоинцидента
     * @param userName имя пользователя
     * @param files переданные в запросе файлы сопроводительных документов
     * @return вид по имени user/accident/create-accident-success
     */
    @PostMapping(value = "/saveAccident", consumes = MULTIPART_FORM_DATA_VALUE)
    public ModelAndView save(@ModelAttribute Accident accident,
                             @RequestParam("car.plate") String carPlate,
                             @RequestParam("type.id") int typeId,
                             @RequestParam("username") String userName,
                             @RequestParam("photo") MultipartFile[] files) {
        accident.setCarPlate(carPlate);
        accident.setType(accidentTypeService.findById(typeId));
        accident.setCreated(LocalDateTime.now());
        accident.setRules(Collections.emptyList());
        accident.setUser(userService.findByUserName(userName));
        accident.setStatus(
                statusService.findById(TrackingStates.ACCEPTED_STATUS.getId())
        );
        documentService.saveDocumentsFromRequest(files,
                                accidentService.add(accident), userName);
        log.info("User(username={}) created a new accident(id={})",
                                userName, accident.getId());
        return new ModelAndView("user/accident/create-accident-success");
    }

    /**
     * Обновляет данные об автоинциденте в хранилище и
     * возвращает сообщение пользователю в случае успешной операции.
     * Возвращает вид user/error/edit-accident-prohibition с сообщением
     * об ошибке, в случае, когда обычный пользователь производит обновление
     * автоинцидента, но в тоже время по этому автоинциденту уже ведутся
     * работы со стороны администратора приложения
     * @param accident автоинцидент
     * @param typeId идентификатор типа автоинцидента
     * @param username имя пользователя
     * @param statusId статус сопровождения автоинцидента
     * @param files массив переданных в запросе файлов
     * @param rIds строковый массив из идентификаторов статей автонарушений
     * @param notice уведомление инициатору автоинцидента
     * @return вид по имени user/accident/edit-accident-success
     */
    @PostMapping(value = "/updateAccident", consumes = MULTIPART_FORM_DATA_VALUE)
    public ModelAndView update(@ModelAttribute Accident accident,
                               @RequestParam("type.id") int typeId,
                               @RequestParam("username") String username,
                               @RequestParam("status.id") int statusId,
                               @RequestParam(value = "photo", required = false) MultipartFile[] files,
                               @RequestParam(value = "rIds", required = false) String[] rIds,
                               @RequestParam(value = "notice", required = false) String notice) {
        boolean isUser = username.equals(getUsername());
        accident.setStatus(statusService.findById(statusId));
        if (isUser && !accidentService.checkAccidentForStatus(accident)) {
            log.error("An attempt by a user with username={} to "
                    + "update an incident(id={}) with an inappropriate status "
                    + "to update!", username, accident.getId());
            return new ModelAndView("user/error/edit-accident-prohibition")
                    .addObject("id", accident.getId());
        }
        accident.setUpdated(LocalDateTime.now());
        accident.setType(accidentTypeService.findById(typeId));
        accident.setRules(ruleService.getRulesFromIds(rIds));
        accident.setUser(userService.findByUserName(username));
        if (statusId == TrackingStates.RETURNED_STATUS.getId()) {
            accident.setStatus(
                    statusService.findById(TrackingStates.ADJUSTED_STATUS.getId())
            );
        }
        accident.setNotice(notice);
        accidentService.update(accident);
        documentService.saveDocumentsFromRequest(files, accident, getUsername());
        return new ModelAndView("admin/accident/edit-accident-success");
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
        log.info("The administrator successfully deleted the incident(id={}) "
                    + "from the archive", id);
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
        log.info("The administrator has successfully completed a full "
                    + "accident archive cleanup!!!");
        return new ModelAndView("admin/accident/delete-all-from-archive-success");
    }

    /**
     * Возвращает вид для отображения сопроводительного документа пользователю
     * @param accidentId идентификатор автоинцидента
     * @param documentId идентификатор сопроводительного документа
     * @return вид по имени user/accident/show-accident-document с данными
     */
    @GetMapping("/accidents/{accidentId}/documents/{documentId}/show")
    public ModelAndView viewShowDocument(@PathVariable("accidentId") int accidentId,
                                         @PathVariable("documentId") int documentId) {
        return new ModelAndView("user/accident/show-accident-document")
                .addObject("accident", accidentService.findById(accidentId))
                .addObject("document", documentService.findById(documentId));
    }

    /**
     * Возвращает вид для отображения карточки учёта автмобиля
     * @param carPlate регистрационный знак автомобиля
     * @return вид по имени /admin/accident/show-reg-card с данными
     */
    @GetMapping("/registration-card")
    public ModelAndView viewShowRegistrationCard(@RequestParam("car_plate") String carPlate) {
        return new ModelAndView("admin/accident/show-reg-card")
                .addObject("card",
                        registrationCardService.findByCarPlate(carPlate));
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
        log.info("Attempt by the user(username={}) to delete the document(id={}) "
                    + "from the accident", getUsername(), documentId);
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
        log.info("The user(username={}) successfully deleted the document (id={}) "
                    + "from the accident", getUsername(), documentId);
        return new ModelAndView("user/accident/delete-accident-document-success.html")
                .addObject("document", documentInDb);
    }

    /**
     * Возвращает вид пользователю с тремя формами для фильтрации автоинцидентов
     * @return вид по имени admin/accident/all-filtered-accidents с
     * заполненными полями выбора Тип и Статус
     */
    @GetMapping("/getFilters")
    public ModelAndView viewGetFilters() {
        return new ModelAndView("admin/accident/all-filtered-accidents")
                .addObject("types", accidentTypeService.findAll())
                .addObject("statuses", statusService.findAll())
                .addObject("filterName", " ");
    }

    /**
     * Возвращает вид пользователю в виде списка отфильтрованных автоинцидентов
     * по типу и статусу сопровождения
     * @param typeId идентификатор типа
     * @param statusId идентификатор статуса сопровождения
     * @return вид по имени admin/accident/all-filtered-accidents с данными
     */
    @GetMapping("/accidents/filter/by_type_and_status")
    public ModelAndView getAllByTypeAndStatus(@RequestParam("type.id") int typeId,
                                              @RequestParam("status.id") int statusId) {
        AccidentType filteredType = accidentTypeService.findById(typeId);
        Status filteredStatus = statusService.findById(statusId);
        String message = String.format(
                "Отфильтрованные инциденты по типу [%s] и статусу [%s]",
                filteredType.getName(), filteredStatus.getName()
        );
        return new ModelAndView("admin/accident/all-filtered-accidents")
                .addObject("accidents",
                        accidentService.findAllByTypeAndStatus(typeId, statusId))
                .addObject("filter3", true)
                .addObject("filterName", message)
                .addObject("filteredType", filteredType)
                .addObject("filteredStatus", filteredStatus)
                .addObject("types", accidentTypeService.findAll())
                .addObject("statuses", statusService.findAll());
    }

    /**
     * Возвращает вид пользователю в виде списка отфильтрованных автоинцидентов
     * по адресу ДТП и диапазону дат, в пределах которого осуществлялась
     * регистрация автоинцидента
     * @param address адрес ДТП
     * @param start нижняя граница диапазона дат в виде локального времени
     * регистрации автоинцидента
     * @param end верхняя граница диапазона дат в виде локального времени
     * регистрации автоинцидента
     * @return вид по имени admin/accident/all-filtered-accidents с данными
     */
    @GetMapping("/accidents/filter/by_address_and_date")
    public ModelAndView getAllByAddressAndDateRange(
                                        @RequestParam("address") String address,
                                        @RequestParam("start") String start,
                                        @RequestParam("end") String end) {
        String message = String.format(
                "Отфильтрованные инциденты по адресу [%s] и дате с [%s] по [%s]",
                address,
                start.replace('T', ' '),
                end.replace('T', ' '));
        return new ModelAndView("admin/accident/all-filtered-accidents")
                .addObject("accidents",
                        accidentService.findAllByAddressAndDateRange(
                                address,
                                LocalDateTime.parse(start, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                LocalDateTime.parse(end, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                ).addObject("filter2", true)
                .addObject("filterName", message)
                .addObject("filteredAddress", address)
                .addObject("types", accidentTypeService.findAll())
                .addObject("statuses", statusService.findAll());
    }

    /**
     * Возвращает вид пользователю в виде списка отфильтрованных автоинцидентов,
     * зарегистрированных в системе за минувшие сутки
     * @return вид по имени admin/accident/all-filtered-accidents с данными
     */
    @GetMapping("/accidents/filter/by_last_day")
    public ModelAndView getAllByRegisteredLastDay() {
        String message = "Инциденты, зарегистрированные в приложении за минувшие сутки";
        return new ModelAndView("admin/accident/all-filtered-accidents")
                .addObject("accidents",
                        accidentService.findAllByRegisteredLastDay())
                .addObject("filterName", message)
                .addObject("types", accidentTypeService.findAll())
                .addObject("statuses", statusService.findAll());
    }
}
