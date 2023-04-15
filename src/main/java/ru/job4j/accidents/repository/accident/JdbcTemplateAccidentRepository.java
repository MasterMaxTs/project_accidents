package ru.job4j.accidents.repository.accident;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.*;

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
     * Объект JdbcTemplate
     * @see ru.job4j.accidents.config.JdbcConfig
     */
    private final JdbcTemplate jdbc;

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
                accident.setResolution(rs.getString("resolution"));
                accident.setStatus(
                        getStatusFromDb(rs.getInt("status_id"))
                );
                accident.setUser(
                        getUserFromDb(rs.getInt("user_id"))
                );
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
                "insert into accidents (name, type_id, text, address, user_id, status_id)"
                        + " values (?, ?, ?, ?, ?, ?)"
        ),
        UPDATE_ACCIDENTS_TABLE(
                "update accidents set"
                        + " name = ?, type_id = ?, text = ?,"
                        + " address = ?, resolution = ?, created = ?,"
                        + " updated = ?, status_id = ?"
                        + " where id = ?"
        ),
        FIND_ACCIDENTS_TABLE(
                "select * from accidents where id = ?"
        ),
        FIND_ALL_BY_USER_ACCIDENTS_TABLE(
                "select * from accidents a where a.user_id = "
                         + "(select id from users where username = ?)"
                         + " order by a.created desc"
        ),
        FIND_ALL_ACCIDENTS_TABLE(
                "select * from accidents order by status_id asc,"
                        + " created desc"
        ),
        FIND_ALL_STATUS_ACCIDENTS_TABLE(
                "select * from accidents where status_id = ?"
                        + " order by created desc, updated desc"
        ),
        FIND_TYPES_TABLE(
                "select * from types where id = ?"
        ),
        FIND_RULES_TABLE(
                "select * from rules where id = ?"
        ),
        FIND_STATUSES_TABLE(
                "select * from statuses where id = ?"
        ),
        FIND_USERS_TABLE(
                "select * from users where id = ?"
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
        DELETE_BY_STATUS_ACCIDENTS_TABLE(
                "delete from accidents where id = ? and status_id = ?"
        ),
        DELETE_ALL_BY_STATUS_ACCIDENTS_TABLE(
                "delete from accidents where status_id = ?"
        ),
        DELETE_DOCUMENTS_TABLE(
                "delete from documents where accident_id = ?"
        ),
        DELETE_ALL_BY_STATUS_DOCUMENTS_TABLE(
                "delete from documents where accident_id in"
                        + "(select id from accidents where status_id = ?)"
        ),
        DELETE_ACCIDENTS_RULES_TABLE(
                "delete from accidents_rules where accident_id = ?"
        ),
        DELETE_ALL_BY_STATUS_ACCIDENTS_RULES_TABLE(
                "delete from accidents_rules where accident_id in"
                + " (select id from accidents where status_id = ?)"
        );

        /**
         * Sql запрос к БД
         */
        private final String sql;
    }

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
                    ps.setInt(5, accident.getUser().getId());
                    ps.setInt(6, accident.getStatus().getId());
                    return ps;
                }, keyHolder);
        accident.setId(keyHolder.getKeyAs(Integer.class));
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
                    accident.getResolution(),
                    accident.getCreated(),
                    accident.getUpdated(),
                    accident.getStatus().getId(),
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
        jdbc.update(
                Queries.DELETE_DOCUMENTS_TABLE.sql,
                accident.getId()
        );
        jdbc.update(
                Queries.DELETE_ACCIDENTS_RULES_TABLE.sql,
                accident.getId());
        jdbc.update(
                Queries.DELETE_ACCIDENTS_TABLE.sql,
                accident.getId());
        System.out.println("Deleting the accident from the database "
                + "successfully");
        return accident;
    }

    @Override
    public List<Accident> findAll() {
        return jdbc.query(Queries.FIND_ALL_ACCIDENTS_TABLE.sql, accidentRowMapper);
    }

    @Override
    public List<Accident> findAllByUserName(String userName) {
        return jdbc.query(
                Queries.FIND_ALL_BY_USER_ACCIDENTS_TABLE.sql,
                accidentRowMapper,
                userName
        );
    }

    @Override
    public List<Accident> findAllQueued() {
        return jdbc.query(
                Queries.FIND_ALL_STATUS_ACCIDENTS_TABLE.sql,
                accidentRowMapper,
                TrackingStates.QUEUED_STATUS.getId());
    }

    @Override
    public List<Accident> findAllArchived() {
        return jdbc.query(
                Queries.FIND_ALL_STATUS_ACCIDENTS_TABLE.sql,
                accidentRowMapper,
                TrackingStates.ARCHIVED_STATUS.getId());
    }

    @Override
    public void deleteAllArchived() {
        jdbc.update(
                Queries.DELETE_ALL_BY_STATUS_DOCUMENTS_TABLE.sql,
                TrackingStates.ARCHIVED_STATUS.getId()
        );
        jdbc.update(
                Queries.DELETE_ALL_BY_STATUS_ACCIDENTS_RULES_TABLE.sql,
                TrackingStates.ARCHIVED_STATUS.getId());
        jdbc.update(
                Queries.DELETE_ALL_BY_STATUS_ACCIDENTS_TABLE.sql,
                TrackingStates.ARCHIVED_STATUS.getId());
        System.out.println("Deleting all archived accidents from the database "
                + "successfully");
    }

    @Override
    public Optional<Accident> findById(int id) {
        return Optional.ofNullable(
                jdbc.queryForObject(
                        Queries.FIND_ACCIDENTS_TABLE.sql, accidentRowMapper, id
                ));
    }

    /**
     * Возвращает тип автоинцидента по id
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
     * Возвращает статус сопровождения автоинцидента по id
     * @param statusId идентификатор статуса сопровождения автоинцидента
     * @return объект AccidentType
     */
    private Status getStatusFromDb(int statusId) {
        return jdbc.queryForObject(
                Queries.FIND_STATUSES_TABLE.sql,
                new BeanPropertyRowMapper<>(Status.class),
                statusId);
    }

    /**
     * Возвращает пользователя (автора) инцидента по id
     * @param userId идентификатор статуса сопровождения автоинцидента
     * @return объект AccidentType
     */
    private User getUserFromDb(int userId) {
        return jdbc.queryForObject(
                Queries.FIND_USERS_TABLE.sql,
                new BeanPropertyRowMapper<>(User.class),
                userId);
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
