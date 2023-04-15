package ru.job4j.accidents.service.accident;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.accident.AccidentPagingAndSortingRepository;
import ru.job4j.accidents.model.TrackingStates;

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
    public Accident checkAccidentForStatus(int accidentId, int statusId) {
        Accident rsl = null;
        Accident accident = findById(accidentId);
        if (statusId == accident.getStatus().getId()) {
            rsl = accident;
        }
        return rsl;
    }

    @Override
    public List<Accident> findAllByUserName(String userName) {
        return store.findAllByUser(userName);
    }

    @Override
    public List<Accident> findAllQueued() {
        return store.findAllByStatus(TrackingStates.QUEUED_STATUS.getId());
    }

    @Override
    public List<Accident> findAllArchived() {
        return store.findAllByStatus(TrackingStates.ARCHIVED_STATUS.getId());
    }

    @Override
    public void deleteAllArchived() {
        store.deleteAllByStatus(TrackingStates.ARCHIVED_STATUS.getId());
    }
}
