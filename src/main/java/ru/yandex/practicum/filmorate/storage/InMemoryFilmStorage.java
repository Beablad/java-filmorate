package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final int MAX_CHAR_DESCRIPTION = 200;
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;
    private Map<Long, Film> filmList = new HashMap<>();


    @Override
    public Film addFilm(Film film) {
        if (validate(film)) {
            film.setId(getNextFilmId());
            filmList.put(film.getId(), film);
            log.info("Фильм добавлен: " + film);
        }
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        log.info("Фильм удален: " + getFilmById(id));
        filmList.remove(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (validate(film)) {
            filmList.put(film.getId(), film);
        }
        log.info("Фильм добавлен/обновлен: " + film);
        return film;
    }

    @Override
    public List<Film> getFilmList() {
        return new ArrayList<>(filmList.values());
    }

    @Override
    public Film getFilmById(int id) {
        return filmList.get(id);
    }

    private int getNextFilmId() {
        return id++;
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
