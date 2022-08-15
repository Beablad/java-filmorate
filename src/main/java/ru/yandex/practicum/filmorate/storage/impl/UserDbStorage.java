package ru.yandex.practicum.filmorate.storage.impl;

import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component("userDbStorage")
@Primary
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        if (validate(user)){
            String sql = "INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDAY)" +
                    "VALUES ( ?, ?, ?, ? )";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            if (user.getName().isBlank()){
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
                    stmt.setString(1, user.getLogin());
                    stmt.setString(2, user.getLogin());
                    stmt.setString(3, user.getEmail());
                    stmt.setDate(4, Date.valueOf(user.getBirthday()));
                    return stmt;
                }, keyHolder);
                user.setName(user.getLogin());
            } else {
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
                    stmt.setObject(1, user.getName());
                    stmt.setString(2, user.getLogin());
                    stmt.setString(3, user.getEmail());
                    stmt.setDate(4, Date.valueOf(user.getBirthday()));
                    return stmt;
                }, keyHolder);
            }
            user.setId(keyHolder.getKey().intValue());
            return user;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public User deleteUser(int id) {
        User user = getUserById(id);
        String sql = "DELETE FROM  USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (validate(user)) {
            String sql = "UPDATE USERS " +
                    "SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? " +
                    "WHERE USER_ID = ?";
            if (user.getName().isBlank()) {
                jdbcTemplate.update(sql, user.getLogin(), user.getLogin(), user.getEmail(),
                        user.getBirthday(), user.getId());
            } else {
                jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(),
                        user.getBirthday(), user.getId());
            }
        } else {
            throw new ValidationException();
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this::mapToRowUser);
    }

    @Override
    public User getUserById(int userId) {
        if (userId<=0){
            throw new IllegalIdException();
        }
        String sql = "SELECT * from USERS where USER_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (sqlRowSet.next()) {
            User user = new User(sqlRowSet.getString("name"), sqlRowSet.getString("login"),
                    sqlRowSet.getString("email"), sqlRowSet.getDate("birthday").toLocalDate());
            user.setId(sqlRowSet.getInt("user_id"));
            return user;
        } else {
            throw new ValidationException();
        }
    }

    User mapToRowUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getString("name"), rs.getString("login"),
                rs.getString("email"), rs.getDate("birthday").toLocalDate());
        user.setId(rs.getInt("user_id"));
        return user;
    }

    private boolean validate(User user) {
        boolean checkEmail = user.getEmail().contains("@") && !user.getEmail().isBlank();
        boolean checkBirthday = user.getBirthday().isBefore(LocalDate.now());
        boolean checkLogin = !user.getLogin().contains(" ") && !user.getLogin().isBlank();
        if (user.getId() < 0) {
            throw new IllegalIdException();
        } else if (checkEmail && checkBirthday && checkLogin) {
            return true;
        } else {
            throw new ValidationException();
        }

    }
}
