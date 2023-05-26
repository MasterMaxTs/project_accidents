package ru.job4j.accidents.repository.accident;

import lombok.NoArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Реализация потокобезопасного локального хранилища Автомобильных инцидентов
 */
@Repository
@NoArgsConstructor
@ThreadSafe
public class MemAccidentRepository implements AccidentRepository {

    /**
     * Хранилище в виде СoncurrentHashMap
     */
    private final Map<String, List<Accident>> accidents = new ConcurrentHashMap<>();

    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    @Override
    public Accident add(Accident accident) {
        String userName = accident.getUser().getUsername();
        accident.setId(atomicInteger.getAndIncrement());
        List<Accident> accidentList = new ArrayList<>();
        accidents.compute(userName,
                            (key, value) -> {
                                                if (value == null) {
                                                     value = accidentList;
                                                }
                                                value.add(accident);
                                                return value;

        });
        return accident;
    }

    @Override
    public boolean update(Accident accident) {
        String userName = accident.getUser().getUsername();
        accidents.computeIfPresent(
                userName,
                    (key, value) -> {
                                value.removeIf(a -> a.getId() == accident.getId());
                                value.add(accident);
                                return value;
                    }
            );
        return true;
    }

    @Override
    public Accident delete(Accident accident) {
        String userName = accident.getUser().getUsername();
        accidents.computeIfPresent(
                userName,
                    (key, value) -> {
                        value.removeIf(a -> a.getId() == accident.getId());
                        return value;
                    }
        );
        return accident;
    }

    /**
     * Возвращает список всех автоинцидентов, находящихся в локальном хранилище
     * @return список всех автоинцидентов, отсортированных по идентификатору
     * статуса сопровождения в естественном порядке
     */
    @Override
    public List<Accident> findAll() {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(a -> a.getStatus().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Accident> findById(int id) {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getId() == id)
                .findFirst();
    }

    /**
     * Возвращает список всех автоинцидентов, принадлежащих конкретному
     * пользователю, находящихся в локальном хранилище
     * @param userName имя пользователя
     * @return список всех автоинцидентов пользователя, отсортированных по
     * идентификатору статуса сопровождения в естественном порядке
     */
    @Override
    public List<Accident> findAllByUserName(String userName) {
        List<Accident> accidentsInMem = accidents.get(userName);
        return accidentsInMem == null ? Collections.emptyList()
                : accidentsInMem
                    .stream()
                    .sorted(Comparator.comparingInt(a -> a.getStatus().getId()))
                    .collect(Collectors.toList());
    }

    @Override
    public List<Accident> findAllByCarPlate(String carPlate) {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getCarPlate().equals(carPlate))
                .sorted(Comparator.comparingInt(a -> a.getStatus().getId()))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список всех автоинцидентов, добавленных в очередь на
     * рассмотрение, находящихся в локальном хранилище данных
     * @return список всех ожидающих решения автоинцидентов, отсортированных по
     * времени создания в естественном порядке
     */
    @Override
    public List<Accident> findAllQueued() {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getStatus().getId() == TrackingStates.QUEUED_STATUS.getId())
                .sorted(Comparator.comparing(Accident::getCreated))
                .collect(Collectors.toList());
    }

    @Override
    public List<Accident> findAllReturned() {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getStatus().getId() == TrackingStates.RETURNED_STATUS.getId())
                .sorted(Comparator.comparing(Accident::getCreated))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список всех автоинцидентов, добавленных в архив,
     * находящихся в локальном хранилище данных
     * @return список всех архивных автоинцидентов, отсортированных по
     * времени решения в обратном порядке
     */
    @Override
    public List<Accident> findAllArchived() {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getStatus().getId() == TrackingStates.ARCHIVED_STATUS.getId())
                .sorted(Comparator.comparing(Accident::getUpdated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllArchived() {
        accidents.entrySet()
                 .stream()
                 .flatMap(entry -> entry.getValue().stream())
                 .filter(a -> a.getStatus().getId() == TrackingStates.ARCHIVED_STATUS.getId())
                 .collect(Collectors.toList())
                 .forEach(this::delete);
    }

    @Override
    public List<Accident> findAllByTypeAndStatus(int typeId, int statusId) {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getType().getId() == typeId && a.getStatus().getId() == statusId)
                .sorted(Comparator.comparing(Accident::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Accident> findAllByAddressAndDateRange(String address,
                                                       LocalDateTime after,
                                                       LocalDateTime before
    ) {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getAddress().equals(address)
                                && (a.getCreated().isAfter(after)
                                && a.getCreated().isBefore(before.plusSeconds(1L))))
                .sorted(Comparator.comparing(Accident::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Accident> findAllByRegisteredLastDay() {
        LocalDateTime current = LocalDateTime.now();
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getCreated().isAfter(current.minusDays(1L))
                        && a.getCreated().isBefore(current.plusSeconds(1L)))
                .sorted(Comparator.comparing(Accident::getCreated).reversed())
                .collect(Collectors.toList());
    }
}
