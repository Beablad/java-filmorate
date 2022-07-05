package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {

    private final int MAX_CHAR_DESCRIPTION = 200;
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;
    Map<Integer, Film> filmList = new HashMap<>();

    private int getFilmId() {
        return id++;
    }

    @GetMapping
    public List<Film> getFilmList() {
        log.info("Получен GET запрос");
        return new ArrayList<>(filmList.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен POST запрос");
        if (validate(film)) {
            film.setId(getFilmId());
            filmList.put(film.getId(), film);
            log.info("Фильм добавлен");
        } else {
            log.warn("Ошибка валидации");
            throw new ValidationException();
        }
        return film;
    }

    @PutMapping
    public Film updateOrAddFilm(@RequestBody Film film) {
        log.info("Получен PUT запрос");
        if (filmList.get(film.getId()) != null && validate(film)) {
            filmList.put(film.getId(), film);
            log.info("Фильм обновлен");
        } else if (validate(film)) {
            film.setId(getFilmId());
            filmList.put(film.getId(), film);
            log.info("Фильм добавлен");
        } else {
            log.warn("Ошибка валидации");
            throw new ValidationException();
        }
        return film;
    }

    private boolean validate(Film film) {
        boolean checkName = !film.getName().isBlank();
        boolean maxCharDescription = film.getDescription().length() < MAX_CHAR_DESCRIPTION;
        boolean minReleaseDate = film.getReleaseDate().isAfter(MIN_RELEASE_DATE);
        boolean isDurationPositive = film.getDuration() > 0;
        if (film.getId() < 0) {
            return false;
        } else if (checkName && maxCharDescription && minReleaseDate && isDurationPositive) {
            return true;
        } else {
            return false;
        }
    }
}
