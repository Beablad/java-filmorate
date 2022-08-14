package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Component
@AllArgsConstructor
public class LikeDbStorage implements LikeStorage {

    JdbcTemplate jdbcTemplate;
    FilmStorage filmStorage;

    @Override
    public void addLike(int filmId, int userId) {
        if (filmId <=0 || userId <=0) {
            throw new IllegalIdException();
        }
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID)" +
                " VALUES ( ?, ? )";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        if (filmId <=0 || userId <=0) {
            throw new IllegalIdException();
        }
        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

}
