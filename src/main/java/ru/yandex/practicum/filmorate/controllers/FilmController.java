package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(value = "/films")
@Slf4j
public class FilmController {

    FilmService filmService;
    private final int MAX_CHAR_DESCRIPTION = 200;
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;
    private Map<Integer, Film> filmList = new HashMap<>();


    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilmList() {
        log.info("Получен GET запрос");
        return filmService.filmStorage.getFilmList();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен POST запрос");
        return filmService.filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateOrAddFilm(@RequestBody Film film) {
        log.info("Получен PUT запрос");
        return filmService.filmStorage.updateFilm(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Film addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(Optional<Integer> count) {
        return filmService.getMostLikesFilm(count);
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.filmStorage.getFilmById(id);
    }
}
