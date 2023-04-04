package ru.job4j.accidents.repository.accident;

import lombok.NoArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.*;
import ru.job4j.accidents.repository.status.StatusRepository;

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
        List<Accident> temp = new ArrayList<>();
        accidents.compute(userName,
                            (key, value) -> {
                                                if (value == null) {
                                                     value = temp;
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

    @Override
    public List<Accident> findAll() {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
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

    @Override
    public List<Accident> findAllByUserName(String userName) {
        List<Accident> accidentsInMem = accidents.get(userName);
        return accidentsInMem == null ? List.of() : new ArrayList<>(accidentsInMem);
    }

    @Override
    public List<Accident> findAllQueued() {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getStatus().getId() == StatusRepository.QUEUED_STATUS_ID)
                .collect(Collectors.toList());
    }

    @Override
    public List<Accident> findAllArchived() {
        return accidents.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getStatus().getId() == StatusRepository.ARCHIVED_STATUS_ID)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllArchived() {
        findAllArchived().clear();
    }
}
