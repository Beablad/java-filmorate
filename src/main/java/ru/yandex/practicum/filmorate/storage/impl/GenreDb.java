package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDb {

    private final JdbcTemplate jdbcTemplate;

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, this::mapToRowGenre);
    }

    public Genre getGenreById(Long id) {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE WHERE GENRE_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()){
            return Genre.builder()
                    .id(sqlRowSet.getLong("GENRE_ID"))
                    .name(sqlRowSet.getString("GENRE_NAME"))
                    .build();
        } else {
            throw new IllegalArgumentException();
        }
    }

    Genre mapToRowGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}
