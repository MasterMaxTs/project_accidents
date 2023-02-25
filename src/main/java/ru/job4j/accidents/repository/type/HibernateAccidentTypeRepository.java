package ru.job4j.accidents.repository.type;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateAccidentTypeRepository implements AccidentTypeRepository {

    private final SessionFactory sf;

    @Override
    public AccidentType add(AccidentType type) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(type);
            session.getTransaction().commit();
        }
        return type;
    }

    @Override
    public boolean update(AccidentType type) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(type);
            session.getTransaction().commit();
        }
        return true;
    }

    @Override
    public AccidentType delete(AccidentType type) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.delete(type);
            session.getTransaction().commit();
        }
        return type;
    }

    @Override
    public List<AccidentType> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "from AccidentType", AccidentType.class)
                    .list();
        }
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        try (Session session = sf.openSession()) {
            return Optional.ofNullable(
                    session.get(AccidentType.class, id)
            );
        }
    }
}
