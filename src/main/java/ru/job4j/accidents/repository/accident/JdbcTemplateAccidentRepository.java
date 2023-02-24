package ru.job4j.accidents.repository.accident;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Реализация доступа к хранилищу автоинцидентов с помощью JdbcTemplate
 */
@Repository
@AllArgsConstructor
public class JdbcTemplateAccidentRepository implements AccidentRepository {

    /**
     * Объект RowMapper
     */
    private final RowMapper<Accident> accidentRowMapper =
            (rs, row) -> {
                Accident accident = new Accident();
                accident.setId(rs.getInt("id"));
                accident.setName(rs.getString("name"));
                accident.setType(
                        getAccidentTypeFromDb(rs.getInt("type_id"))
                );
                accident.setText(rs.getString("text"));
                accident.setRules(
                        getAccidentRulesFromDb(rs.getInt("id"))
                );
                accident.setAddress(rs.getString("address"));
                accident.setCreated(rs.getTimestamp("created").toLocalDateTime());
                Timestamp updatedTime = rs.getTimestamp("updated");
                if (updatedTime != null) {
                    accident.setUpdated(updatedTime.toLocalDateTime());
                }
                return accident;
            };

    /**
     * Набор констант, содержащих SQL запросы к БД
     */
    @AllArgsConstructor
    enum Queries {
        INSERT_ACCIDENTS_TABLE(
                "insert into accidents (name, type_id, text, address)"
                        + " values (?, ?, ?, ?)"
        ),
        UPDATE_ACCIDENTS_TABLE(
                "update accidents set"
                        + " name = ?, type_id = ?, text = ?,"
                        + " address = ?, created = ?, updated = ? where id = ?"
        ),
        FIND_ACCIDENTS_TABLE(
                "select * from accidents where id = ?"
        ),
        FIND_ALL_ACCIDENTS_TABLE(
                "select * from accidents order by created desc, updated desc"
        ),
        FIND_TYPES_TABLE(
                "select * from types where id = ?"
        ),
        FIND_RULES_TABLE(
                "select * from rules where id = ?"
        ),
        FIND_ACCIDENTS_RULES_TABLE(
                "select * from rules where id IN"
                + " (select rule_id from accidents_rules where accident_id = ?)"
        ),
        INSERT_ACCIDENTS_RULES_TABLE(
                "insert into accidents_rules (accident_id, rule_id)"
                        + " values(?, ?)"
        ),
        DELETE_ACCIDENTS_TABLE(
                "delete from accidents where id = ?"
        ),
        DELETE_ACCIDENTS_RULES_TABLE(
                "delete from accidents_rules where accident_id = ?"
        );

        /**
         * Sql запрос к БД
         */
        private final String sql;
    }

    /**
     * Объект JdbcTemplate
     * @see ru.job4j.accidents.config.JdbcConfig
     */
    private final JdbcTemplate jdbc;

    @Override
    public Accident add(Accident accident) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            Queries.INSERT_ACCIDENTS_TABLE.sql,
                            new String[]{"id"}
                    );
                    ps.setString(1, accident.getName());
                    ps.setInt(2, accident.getType().getId());
                    ps.setString(3, accident.getText());
                    ps.setString(4, accident.getAddress());
                    return ps;
                }, keyHolder);
        accident.setId(keyHolder.getKeyAs(Integer.class));
        accident.getRules()
                .forEach(
                        rule -> jdbc.update(
                                Queries.INSERT_ACCIDENTS_RULES_TABLE.sql,
                                accident.getId(),
                                rule.getId())
                );
        System.out.println("Inserting a new accident into the database is"
                + " successful!");
        return accident;
    }

    @Override
    public boolean update(Accident accident) {
        jdbc.update(Queries.UPDATE_ACCIDENTS_TABLE.sql,
                    accident.getName(),
                    accident.getType().getId(),
                    accident.getText(),
                    accident.getAddress(),
                    accident.getCreated(),
                    accident.getUpdated(),
                    accident.getId()
        );
        jdbc.update(Queries.DELETE_ACCIDENTS_RULES_TABLE.sql,
                    accident.getId());
        accident.getRules()
                .forEach(
                        rule -> jdbc.update(
                                Queries.INSERT_ACCIDENTS_RULES_TABLE.sql,
                                accident.getId(),
                                rule.getId())
                );
        System.out.println("Updating a accident in the database is"
                + " successful!");
        return true;
    }

    @Override
    public Accident delete(Accident accident) {
        jdbc.update(Queries.DELETE_ACCIDENTS_RULES_TABLE.sql,
                accident.getId());
        jdbc.update(Queries.DELETE_ACCIDENTS_TABLE.sql, accident.getId());
        System.out.println("Deleting the accident from the database "
                + "successfully");
        return accident;
    }

    @Override
    public List<Accident> findAll() {
        return jdbc.query(Queries.FIND_ALL_ACCIDENTS_TABLE.sql, accidentRowMapper);
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(
                jdbc.queryForObject(
                        Queries.FIND_ACCIDENTS_TABLE.sql, accidentRowMapper, id
                ));
    }

    /**
     * Возвращает объект AccidentType, искомый в БД по id
     * @param typeId идентификатор типа автоинцидента
     * @return объект AccidentType
     */
    private AccidentType getAccidentTypeFromDb(int typeId) {
        return jdbc.queryForObject(
                Queries.FIND_TYPES_TABLE.sql,
                new BeanPropertyRowMapper<>(AccidentType.class),
                typeId);
    }

    /**
     * Возвращает коллекцию уникальных объектов в виде статей нарушений из БД,
     * как свойство объекта Accident
     * @param accidentId идентификатор автоинцидента
     * @return коллекцию уникальных объектов в виде статей нарушений
     */
    private List<Rule> getAccidentRulesFromDb(int accidentId) {
        return jdbc.query(
                            Queries.FIND_ACCIDENTS_RULES_TABLE.sql,
                            new BeanPropertyRowMapper<>(Rule.class),
                            accidentId
        );
    }
}
