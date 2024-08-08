package dao;

import mappers.UserRowMapper;
import models.Role;
import models.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public User getUser(String email) {
        String sql = "select u.id as user_id, " +
                "       username, " +
                "       email, " +
                "       password, " +
                "       r.id as role_id, " +
                "       r.name as role " +
                "from w.users as u " +
                "inner join w.roles as r on r.id = u.role_id " +
                "where email = :email";

        return namedJdbcTemplate.query(sql, new MapSqlParameterSource()
                .addValue("email", email),
                new UserRowMapper())
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Role getRoleOfUser(long userId) {
        String sql = "select r.* from w.roles as r " +
                "inner join w.users as u on r.id = u.role_id " +
                "where u.id = :userId";

        return namedJdbcTemplate.query(sql, new MapSqlParameterSource()
                .addValue("userId", userId),
                new BeanPropertyRowMapper<>(Role.class))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
