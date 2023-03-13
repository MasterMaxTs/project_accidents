package ru.job4j.accidents.repository.type;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * Реализация доступа к хранилищу Типов автоинцидентов с помощью JdbcTemplate
 */
@Repository
@AllArgsConstructor
public class JdbcTemplateAccidentTypeRepository implements AccidentTypeRepository {

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
        INSERT_TYPES_TABLE(
                "insert into types (name) values (?)"
        ),
        UPDATE_TYPES_TABLE(
                "update types set name = ? where id = ?"
        ),
        DELETE_TYPES_TABLE(
                "delete from types where id = ?"
        ),
        FIND_TYPES_TABLE(
                "select * from types where id = ?"
        ),
        FIND_ALL_TYPES_TABLE(
                "select * from types"
        );

        /**
         * Sql запрос к БД
         */
        private final String sql;
    }

    @Override
    public AccidentType add(AccidentType type) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            Queries.INSERT_TYPES_TABLE.sql, new String[]{"id"}
                    );
                    ps.setString(1, type.getName());
                    return ps;
                }, keyHolder
        );
        type.setId(keyHolder.getKeyAs(Integer.class));
        return type;
    }

    @Override
    public boolean update(AccidentType type) {
        boolean rsl = false;
        if (jdbc.update(
                        Queries.UPDATE_TYPES_TABLE.sql,
                        type.getName(),
                        type.getId()) > 0) {
            System.out.println("Updating a accident type in the database is"
                    + " successful!");
            rsl = true;
        }
        return rsl;
    }

    @Override
    public AccidentType delete(AccidentType type) {
        jdbc.update(Queries.DELETE_TYPES_TABLE.sql, type.getId());
        System.out.println("Deleting the accident type from the database "
                + "successfully");
        return type;
    }

    @Override
    public List<AccidentType> findAll() {
        return jdbc.query(
                        Queries.FIND_ALL_TYPES_TABLE.sql,
                        new BeanPropertyRowMapper<>(AccidentType.class)
        );
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        return Optional.ofNullable(
                    jdbc.queryForObject(
                            Queries.FIND_TYPES_TABLE.sql,
                            new BeanPropertyRowMapper<>(AccidentType.class),
                            id
                    )
        );
    }
}
