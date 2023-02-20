package ru.job4j.accidents.repository.accident;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Реализация потокобезопасного локального хранилища Автомобильных инцидентов
 */
@Repository
@ThreadSafe
public class MemAccidentRepository implements AccidentRepository {

    /**
     * Хранилище в виде СoncurrentHashMap
     */
    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    public MemAccidentRepository() {
        init();
    }

    /**
     * Инициализирует хранилище начальными данными
     */
    private void init() {
        add(new Accident(
                "AccidentName1",
                new AccidentType(1, "Две машины"),
                "AccidentText1",
                Set.of(new Rule(1, "Статья. 1")),
                "AccidentAddress1"));
        add(new Accident(
                "AccidentName2",
                new AccidentType(2, "Машина и человек"),
                "AccidentText2",
                Set.of(new Rule(2, "Статья. 2"),
                       new Rule(3, "Статья. 3")),
                "AccidentAddress2"));
        add(new Accident(
                "AccidentName3",
                new AccidentType(3, "Машина и велосипед"),
                "AccidentText3",
                Set.of(new Rule(4, "Статья. 4")),
                "AccidentAddress3"));
    }

    @Override
    public Accident add(Accident accident) {
        accident.setId(atomicInteger.getAndIncrement());
        return accidents.putIfAbsent(accident.getId(), accident);
    }

    @Override
    public boolean update(Accident accident) {
        return accidents.replace(accident.getId(), accident) != null;
    }

    @Override
    public Accident delete(Accident accident) {
        return accidents.remove(accident.getId());
    }

    @Override
    public List<Accident> findAll() {
        return new ArrayList<>(accidents.values());
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(accidents.get(id));
    }
}
