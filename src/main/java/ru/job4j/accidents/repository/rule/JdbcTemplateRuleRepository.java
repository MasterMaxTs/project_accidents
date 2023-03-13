package ru.job4j.accidents.repository.rule;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * Реализация доступа к хранилищу статей нарушений с помощью JdbcTemplate
 */
@Repository
@AllArgsConstructor
public class JdbcTemplateRuleRepository implements RuleRepository {

    /**
     * Объект JdbcTemplate
     * @see ru.job4j.accidents.config.JdbcConfig
     */
    private final JdbcTemplate jdbc;

    /**
     * Набор констант, содержащих SQL запросы к БД
     */
    @AllArgsConstructor
    enum Queries {
        INSERT_RULES_TABLE(
                "insert into rules (name) values (?)"
        ),
        UPDATE_RULES_TABLE(
                "update rules set name = ? where id = ?"
        ),
        DELETE_RULES_TABLE(
                "delete from rules where id = ?"
        ),
        FIND_RULES_TABLE(
                "select * from rules where id = ?"
        ),
        FIND_ALL_RULES_TABLE(
                "select * from rules"
        );

        /**
         * Sql запрос к БД
         */
        private final String sql;
    }

    @Override
    public Rule add(Rule rule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            Queries.INSERT_RULES_TABLE.sql, new String[]{"id"}
                    );
                    ps.setString(1, rule.getName());
                    return ps;
                }
        );
        rule.setId(keyHolder.getKeyAs(Integer.class));
        return rule;
    }

    @Override
    public boolean update(Rule rule) {
        boolean rsl = false;
        if (jdbc.update(
                        Queries.UPDATE_RULES_TABLE.sql,
                        rule.getName(),
                        rule.getId()) > 0) {
            System.out.println("Updating a rule in the database is"
                    + " successful!");
            rsl = true;
        }
        return rsl;
    }

    @Override
    public Rule delete(Rule rule) {
        jdbc.update(Queries.DELETE_RULES_TABLE.sql, rule.getId());
        System.out.println("Deleting the rule from the database "
                + "successfully");
        return rule;
    }

    @Override
    public List<Rule> findAll() {
        return jdbc.query(
                Queries.FIND_ALL_RULES_TABLE.sql,
                new BeanPropertyRowMapper<>(Rule.class)
        );
    }

    @Override
    public Optional<Rule> findById(int id) {
        return Optional.ofNullable(
                jdbc.queryForObject(
                        Queries.FIND_RULES_TABLE.sql,
                        new BeanPropertyRowMapper<>(Rule.class),
                        id
                )
        );
    }
}
