package ru.job4j.accidents.repository.status;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Status;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * Реализация доступа к хранилищу Статусов сопровождения автоинцидентов с
 * помощью JdbcTemplate
 */
@Repository
@AllArgsConstructor
public class JdbcTemplateStatusRepository implements StatusRepository {

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
        INSERT_STATUSES_TABLE(
                "insert into statuses (name) values (?)"
        ),
        UPDATE_STATUSES_TABLE(
                "update statuses set name = ? where id = ?"
        ),
        DELETE_STATUSES_TABLE(
                "delete from statuses where id = ?"
        ),
        FIND_STATUSES_TABLE(
                "select * from statuses where id = ?"
        ),
        FIND_ALL_STATUSES_TABLE(
                "select * from statuses"
        );

        /**
         * Sql запрос к БД
         */
        private final String sql;
    }

    @Override
    public Status add(Status status) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            JdbcTemplateStatusRepository.Queries.INSERT_STATUSES_TABLE.sql,
                            new String[]{"id"}
                    );
                    ps.setString(1, status.getName());
                    return ps;
                }, keyHolder
        );
        status.setId(keyHolder.getKeyAs(Integer.class));
        return status;
    }

    @Override
    public boolean update(Status status) {
        boolean rsl = false;
        if (jdbc.update(
                Queries.UPDATE_STATUSES_TABLE.sql,
                status.getName(),
                status.getId()) > 0) {
            System.out.println("Updating a status in the database is"
                    + " successful!");
            rsl = true;
        }
        return rsl;
    }

    @Override
    public Status delete(Status status) {
        jdbc.update(Queries.DELETE_STATUSES_TABLE.sql, status.getId());
        System.out.println("Deleting the status from the database "
                + "successfully");
        return status;
    }

    @Override
    public List<Status> findAll() {
        return jdbc.query(
                Queries.FIND_ALL_STATUSES_TABLE.sql,
                new BeanPropertyRowMapper<>(Status.class)
        );
    }

    @Override
    public Optional<Status> findById(int id) {
        return Optional.ofNullable(
                    jdbc.queryForObject(
                            Queries.FIND_STATUSES_TABLE.sql,
                            new BeanPropertyRowMapper<>(Status.class),
                            id
                    )
        );
    }
}
