package ru.job4j.accidents.repository.accident;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.status.StatusRepository;

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
            session.merge(accident);
            session.getTransaction().commit();
        }
        return true;
    }

    @Override
    public Accident delete(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Accident accidentInDb = session.get(Accident.class, accident.getId());
            session.delete(accidentInDb);
            session.getTransaction().commit();
        }
        return accident;
    }

    @Override
    public List<Accident> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select distinct a from Accident a left join fetch a.rules"
                            + " order by a.status.id asc, a.created desc, a.updated desc",
                    Accident.class)
                    .list();
        }
    }

    @Override
    public List<Accident> findAllByUserName(String userName) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
            " select distinct a from Accident a left join fetch a.rules"
                    + " where a.user.username =:fUsername"
                    + " order by a.status.id asc, a.created desc, a.updated desc",
                        Accident.class)
                        .setParameter("fUsername", userName)
                        .list();
        }
    }

    @Override
    public List<Accident> findAllQueued() {
        try (Session session = sf.openSession()) {
          return session.createQuery(
                  "select distinct a from Accident a left join fetch a.rules"
                          + " where a.status.id = :stId", Accident.class)
                  .setParameter("stId", StatusRepository.QUEUED_STATUS_ID)
                  .list();
        }
    }

    @Override
    public List<Accident> findAllArchived() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select distinct a from Accident a left join fetch a.rules"
                            + " where a.status.id = :stId"
                            + " order by a.updated desc", Accident.class)
                    .setParameter("stId", StatusRepository.ARCHIVED_STATUS_ID)
                    .list();
        }
    }

    @Override
    public void deleteAllArchived() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.createQuery(
                    "delete from Accident a where a.status.id = "
                            + StatusRepository.ARCHIVED_STATUS_ID)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "from Accident a left join fetch a.rules where a.id = :fId",
                     Accident.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
        }
    }
}
