package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("usersRepository")
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(JdbcTemplate jdbcTemplate) {
       this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findById(Long id) {
        final String SQL = "SELECT * FROM users WHERE id = ? ;";

        User user = DataAccessUtils.singleResult(jdbcTemplate.query(SQL, new UserMapper(), id));

        return user;
    }

    @Override
    public List<User> findAll() {
        final String SQL = "SELECT * FROM users;";
        return jdbcTemplate.query(SQL, new UserMapper());
    }

    @Override
    public void save(User entity) {
        final String SQL = "INSERT INTO users(login, password) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SQL, new String[]{"id"});
            ps.setString(1, entity.getLogin());
            ps.setString(2, entity.getPassword());
            return ps;
        }, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(User entity) {
        final String SQL = "UPDATE users SET login =  ?, password = ? WHERE id = ?;";

        jdbcTemplate.update(SQL, entity.getLogin(), entity.getPassword(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        final String SQL = "DELETE FROM users WHERE id = ? ;";

       jdbcTemplate.update(SQL, id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        final String SQL = "SELECT * FROM users WHERE login = ? ;";

        User user = DataAccessUtils.singleResult(jdbcTemplate.query(SQL, new UserMapper(), login));

        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    private static class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getLong("id"), rs.getString("login"), rs.getString("password"));
            return user;
        }
    }

}