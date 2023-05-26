package ru.job4j.accidents.service.accident;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Status;
import ru.job4j.accidents.repository.accident.AccidentPagingAndSortingRepository;
import ru.job4j.accidents.model.TrackingStates;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Реализация сервиса Автомобильных инцидентов
 */
@Service
@AllArgsConstructor
public class AccidentDataService implements AccidentService {

    /**
     * Делегирование выполнения операций Spring Data при доступе к хранилищу
     * Автомобильных инцидентов
     */
    private final AccidentPagingAndSortingRepository store;

    @Override
    public Accident add(Accident accident) {
        return store.save(accident);
    }

    @Override
    public boolean update(Accident accident) {
        store.save(accident);
        return true;
    }

    @Transactional
    @Override
    public Accident delete(Accident accident) {
        store.deleteAccidentById(accident.getId());
        return accident;
    }

    /**
     * Возвращает список всех автоинцидентов из БД, отсортированных по
     * id статуса сопровождения, далее по локальному времени создания от
     * позднего к раннему
     * @return отсортированный список всех автоинцидентов
     */
    @Override
    public List<Accident> findAll() {
        return (List<Accident>) store.findAll(
                Sort.by("status_id").ascending()
                        .and(Sort.by("created").ascending())
        );
    }

    @Override
    public Accident findById(int id) {
        return store.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                    String.format("Accident with id = %d not found in store", id)
                ));
    }

    @Override
    public boolean checkAccidentForStatus(Accident accident) {
        Accident accidentInDb = findById(accident.getId());
        return accident.getStatus() == accidentInDb.getStatus();
    }

    @Transactional
    @Override
    public List<Accident> findAllByTypeAndStatus(int typeId, int statusId) {
        return store.findAllByType_IdAndStatus_IdOrderByCreatedDesc(typeId, statusId);
    }

    @Transactional
    @Override
    public List<Accident> findAllByAddressAndDateRange(String address,
                                                       LocalDateTime after,
                                                       LocalDateTime before
    ) {
        return store.findAllByAddressAndCreatedBetweenOrderByCreatedDesc(address,
                                                                         after,
                                                                         before);
    }

    @Transactional
    @Override
    public List<Accident> findAllByRegisteredLastDay() {
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime after = before.minusDays(1L);
        return store.findAllByCreatedBetweenOrderByCreatedDesc(after, before);
    }

    @Transactional
    @Override
    public List<Accident> findAllByUserName(String userName) {
        return store.findAllByUser_UsernameOrderByStatusAscCreatedDesc(userName);
    }

    @Transactional
    @Override
    public List<Accident> findAllByCarPlate(String carPlate) {
        return store.findAllByCarPlateOrderByStatusAscCreatedDesc(carPlate);
    }

    @Transactional
    @Override
    public List<Accident> findAllQueued() {
        return store.findAllByStatus_Id(TrackingStates.QUEUED_STATUS.getId());
    }

    @Override
    public List<Accident> findAllReturned() {
        return store.findAllByStatus_Id(TrackingStates.RESOLVED_STATUS.getId());
    }

    @Transactional
    @Override
    public List<Accident> findAllArchived() {
        return store.findAllByStatus_Id(TrackingStates.ARCHIVED_STATUS.getId());
    }

    @Transactional
    @Override
    public void deleteAllArchived() {
        store.deleteAllByStatus_Id(TrackingStates.ARCHIVED_STATUS.getId());
    }
}
