package ru.job4j.accidents.repository.accident;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateAccidentRepository implements AccidentRepository {

    private final SessionFactory sf;

    @Override
    public Accident add(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(accident);
            session.getTransaction().commit();
        }
        return accident;
    }

    @Override
    public boolean update(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(accident);
            session.getTransaction().commit();
        }
        return true;
    }

    @Override
    public Accident delete(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.delete(accident);
            session.getTransaction().commit();
        }
        return accident;
    }

    @Override
    public List<Accident> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "from Accident order by created desc, updated desc",
                    Accident.class)
                    .list();
        }
    }

    @Override
    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            return Optional.ofNullable(
                    session.get(Accident.class, id)
            );
        }
    }
}
