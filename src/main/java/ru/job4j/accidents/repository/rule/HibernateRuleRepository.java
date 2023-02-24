package ru.job4j.accidents.repository.rule;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateRuleRepository implements RuleRepository {

    private final SessionFactory sf;

    @Override
    public Rule add(Rule rule) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(rule);
            session.getTransaction().commit();
        }
        return rule;
    }

    @Override
    public boolean update(Rule rule) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(rule);
            session.getTransaction().commit();
        }
        return true;
    }

    @Override
    public Rule delete(Rule rule) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.delete(rule);
            session.getTransaction().commit();
        }
        return rule;
    }

    @Override
    public List<Rule> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "from Rule", Rule.class)
                    .list();
        }
    }

    @Override
    public Optional<Rule> findById(int id) {
        try (Session session = sf.openSession()) {
            return Optional.ofNullable(
                    session.get(Rule.class, id)
            );
        }
    }
}
