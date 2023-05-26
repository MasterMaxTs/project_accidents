package ru.job4j.accidents.repository.accident;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.TrackingStates;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Реализация доступа к хранилищу автоинцидентов с помощью Hibernate
 */
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
            session.delete(session.get(Accident.class, accident.getId()));
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
                    Accident.class
                    ).list();
        }
    }

    @Override
    public List<Accident> findAllByUserName(String userName) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
            " select distinct a from Accident a left join fetch a.rules"
                    + " where a.user.username =:fUsername"
                    + " order by a.status.id asc, a.created desc, a.updated desc",
                        Accident.class
                    ).setParameter("fUsername", userName)
                    .list();
        }
    }

    @Override
    public List<Accident> findAllByCarPlate(String carPlate) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                "select distinct a from Accident a left join fetch a.rules"
                    + " where a.carPlate =:fCarPlate"
                    + " order by a.status.id asc, a.created desc, a.updated desc",
                        Accident.class
                    ).setParameter("fCarPlate", carPlate)
                    .list();
        }
    }

    @Override
    public List<Accident> findAllQueued() {
        try (Session session = sf.openSession()) {
          return session.createQuery(
                  "select distinct a from Accident a left join fetch a.rules"
                          + " where a.status.id = :statusId", Accident.class
                  ).setParameter("statusId", TrackingStates.QUEUED_STATUS.getId())
                  .list();
        }
    }

    @Override
    public List<Accident> findAllReturned() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                            "select distinct a from Accident a left join fetch a.rules"
                                    + " where a.status.id = :statusId", Accident.class
                    ).setParameter("statusId", TrackingStates.RETURNED_STATUS.getId())
                    .list();
        }
    }

    @Override
    public List<Accident> findAllArchived() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select distinct a from Accident a left join fetch a.rules"
                            + " where a.status.id = :statusId"
                            + " order by a.updated desc", Accident.class
                    ).setParameter("statusId", TrackingStates.ARCHIVED_STATUS.getId())
                    .list();
        }
    }

    @Override
    public void deleteAllArchived() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.createQuery(
                    "delete from Accident a where a.status.id = "
                            + TrackingStates.ARCHIVED_STATUS.getId()
                    ).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Accident> findAllByTypeAndStatus(int typeId, int statusId) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select a from Accident a left join fetch a.rules where"
                            + " a.type.id = :typeId and a.status.id = :statusId"
                    + " order by a.created desc", Accident.class
            ).setProperties(
                    Map.of("typeId", typeId,
                           "statusId", statusId)
            ).list();
        }
    }

    @Override
    public List<Accident> findAllByAddressAndDateRange(String address,
                                                        LocalDateTime after,
                                                        LocalDateTime before
    ) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select distinct a from Accident a left join fetch a.rules"
                            + " where"
                            + " a.address = :fAddress"
                            + " and a.created between :after and :before"
                            + " order by a.created desc", Accident.class
            ).setProperties(
                    Map.of("fAddress", address,
                            "after", after.minusSeconds(1L),
                            "before", before)
            ).list();
        }
    }

    @Override
    public List<Accident> findAllByRegisteredLastDay() {
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime after = before.minusDays(1L).minusSeconds(1L);
        try (Session session = sf.openSession()) {
            return session.createQuery(
                            "select distinct a from Accident a left join fetch a.rules"
                                    + " where"
                                    + " a.created between :after and :before"
                                    + " order by a.created desc", Accident.class
                    ).setProperties(
                            Map.of("after", after,
                                   "before", before)
                    ).list();
        }
    }

    @Override
    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                        "select a from Accident a left join fetch a.rules "
                                + " where a.id = :fId", Accident.class
                    ).setParameter("fId", id)
                    .uniqueResultOptional();
        }
    }
}
