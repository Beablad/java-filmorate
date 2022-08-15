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
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            KeyHolder keyHolder = new GeneratedKeyHolder();
            if (film.getMpa() == null){
                throw new ValidationException();
            } else {
                String sql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) " +
                        "VALUES ( ?, ?, ?, ? , ?) ";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
                    stmt.setString(1, film.getName());
                    stmt.setString(2, film.getDescription());
                    stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                    stmt.setLong(4, film.getDuration());
                    stmt.setLong(5, film.getMpa().getId());
                    return stmt;
                }, keyHolder);
            }
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
                    "SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_RATING_ID = ?" +
                    "WHERE FILM_ID = ?";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    film.getMpa().getId(), film.getId());
            addGenresToFilm(film);
            return getFilmById(film.getId());
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
    public Film getFilmById(long id) {
        String sqlQueryFilm = "SELECT * FROM FILMS where FILM_ID = ?";
        SqlRowSet srsFilm = jdbcTemplate.queryForRowSet(sqlQueryFilm, id);
        String sqlFilmGenre = "SELECT  * FROM FILM_GENRE where FILM_ID = ?";
        LinkedHashSet<Genre> genreSet = new HashSet<>(jdbcTemplate.query(sqlFilmGenre, this::mapToRowGenre, id))
                .stream()
                .sorted(Comparator.comparingLong(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (srsFilm.next()) {
            long mpa_id = srsFilm.getInt("mpa_rating_id");
            MpaRating mpa = mpaRatingDb.getMpaById(mpa_id);
            Film film = new Film(srsFilm.getString("name"), srsFilm.getString("description"),
                    srsFilm.getDate("release_date").toLocalDate(), srsFilm.getInt("duration"),
                    genreSet,
                    mpa);
            for (Genre genre : film.getGenres()) {
                String sqlGenre = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
                SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGenre, genre.getId());
                if (sqlRowSet.next()) {
                    genre.setName(sqlRowSet.getString("genre_name"));
                }
            }
            film.setId(srsFilm.getInt("film_id"));
            return film;
        } else {
            throw new ValidationException();
        }
    }

    private Film mapToRowFilm(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM FILM_GENRE WHERE FILM_ID = ?";
        MpaRating mpa = mpaRatingDb.getMpaById(rs.getLong("mpa_rating_id"));
        Film film = new Film(rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(jdbcTemplate.query(sql, this::mapToRowGenre, rs.getInt("film_id")))
                        .stream()
                        .sorted(Comparator.comparingLong(Genre::getId))
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                mpaRatingDb.getMpaById(mpa.getId()));
        for (Genre genre : film.getGenres()) {
            String sqlGenre = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGenre, genre.getId());
            if (sqlRowSet.next()) {
                genre.setName(sqlRowSet.getString("genre_name"));
            }
        }

        film.setId(rs.getInt("film_id"));
        return addLikeToFilm(film);
    }

    private Genre mapToRowGenre(ResultSet rs, int rowNum) throws SQLException {
        /*String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        jdbcTemplate.queryForRowSet(sql, rs.getInt("GENRE_ID"))*/
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .build();
    }

    private void addGenresToFilm(Film film) {
        String sqlDeleteGenre = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDeleteGenre, film.getId());
        Set<Genre> set = film.getGenres();
        if (set != null) {
            for (Genre genre : set) {
                String sql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) values ( ?, ? )";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
            for (Genre genre : film.getGenres()) {
                String sqlGenre = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
                SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGenre, genre.getId());
                if (sqlRowSet.next()) {
                    genre.setName(sqlRowSet.getString("genre_name"));
                }
            }
        }
    }

    private Film addLikeToFilm (Film film) {
        String sql = "SELECT * FROM LIKES WHERE FILM_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (sqlRowSet.next()){
            film.addLike(new Like(sqlRowSet.getInt("like_id"), sqlRowSet.getInt("film_id"), sqlRowSet.getInt("user_id")));
        }
        return film;
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
