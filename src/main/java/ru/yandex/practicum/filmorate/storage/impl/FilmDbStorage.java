package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    MpaRatingDb mpaRatingDb;
    private final int MAX_CHAR_DESCRIPTION = 200;
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film addFilm(Film film) {
        if (validate(film)) {
            String sql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) " +
                    "VALUES ( ?, ?, ?, ? , ?) ";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setLong(4, film.getDuration());
                stmt.setLong(5, film.getMpa().getId());
                return stmt;
            }, keyHolder);
            film.setId(keyHolder.getKey().intValue());
            addGenresToFilm(film);
            return film;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public void deleteFilm(int id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (validate(film)) {
            String sql = "UPDATE FILMS " +
                    "SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ? " +
                    "WHERE FILM_ID = ?";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    film.getId());
            return film;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public List<Film> getFilmList() {
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, this::mapToRowFilm);
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * FROM FILMS where FILM_ID = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, id);
        if (srs.next()) {
            long mpa_id = srs.getInt("mpa_rating_id");
            MpaRating mpa = mpaRatingDb.getMpaById(mpa_id);
            Film film = new Film(srs.getString("name"), srs.getString("description"),
                    srs.getDate("release_date").toLocalDate(), srs.getInt("duration"),
                    List.of(Genre.builder().build()), mpa);
            //TODO genre
            film.setId(srs.getInt("film_id"));
            return film;
        } else {
            throw new ValidationException();
        }
    }

    public List<Film> getMostLikeFilms(int count) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date,"
                + " f.duration, f.MPA_RATING_ID"
                + " FROM films AS f"
                + " LEFT JOIN likes AS l on f.film_id = l.film_id"
                + " LEFT JOIN film_genre AS fg on f.film_id = fg.film_id"
                + " WHERE %s"
                + " GROUP BY f.film_id"
                + " ORDER BY COUNT(DISTINCT l.user_id)"
                + " LIMIT ?";
        return jdbcTemplate.query(sql, this::mapToRowFilm, count);
    }

    Film mapToRowFilm(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM FILM_GENRE WHERE FILM_ID = ?";
        MpaRating mpa = mpaRatingDb.getMpaById(rs.getLong("mpa_rating_id"));
        Film film = new Film(rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                jdbcTemplate.query(sql, this::mapToRowGenre, rs.getInt("film_id")),
                mpa);
        film.setId(rs.getInt("film_id"));
        return film;
    }

    Genre mapToRowGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .build();
    }

    private void addGenresToFilm(Film film) {
        List<Genre> list = film.getGenres();
        if (list!=null){
            for (Genre genre : list) {
                String sql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) values ( ?, ? )";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
    }

    private boolean validate(Film film) {
        boolean checkName = !film.getName().isBlank();
        boolean maxCharDescription = film.getDescription().length() < MAX_CHAR_DESCRIPTION;
        boolean minReleaseDate = film.getReleaseDate().isAfter(MIN_RELEASE_DATE);
        boolean isDurationPositive = film.getDuration() > 0;
        if (film.getId() < 0) {
            throw new IllegalIdException();
        } else if (checkName && maxCharDescription && minReleaseDate && isDurationPositive) {
            return true;
        } else {
            throw new ValidationException();
        }
    }
}
