package ru.job4j.accidents.repository.status;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Status;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateStatusRepository extends StatusRepository {

    private final SessionFactory sf;

    @Override
    public Status add(Status status) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(status);
            session.getTransaction().commit();
        }
        return status;
    }

    @Override
    public boolean update(Status status) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.merge(status);
            session.getTransaction().commit();
        }
        return true;
    }

    @Override
    public Status delete(Status status) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Status statusInDb = session.get(Status.class, status.getId());
            session.remove(statusInDb);
            session.getTransaction().commit();
        }
        return status;
    }

    @Override
    public List<Status> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery("from Status", Status.class).list();
        }
    }

    @Override
    public Optional<Status> findById(int id) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "from Status s where s.id = :fStId", Status.class)
                    .setParameter("fStId", id)
                    .uniqueResultOptional();
        }
    }
}
