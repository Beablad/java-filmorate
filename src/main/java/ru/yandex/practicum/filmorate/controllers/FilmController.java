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

    private final FilmService filmService;


    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilmList() {
        log.info("Получен GET запрос");
        return filmService.getFilmList();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен POST запрос");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateOrAddFilm(@RequestBody Film film) {
        log.info("Получен PUT запрос");
        return filmService.updateFilm(film);
    }

    @DeleteMapping(value = "/{id}")
    public Film deleteFilm(@PathVariable int id) {
        Film film = filmService.getFilmById(id);
        filmService.deleteFilm(id);
        return film;
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Film addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        Film film = filmService.getFilmById(id);
        filmService.addLike(id, userId);
        return film;
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        Film film = filmService.getFilmById(id);
        filmService.deleteLike(id, userId);
        return film;
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getMostLikesFilm(Objects.requireNonNullElse(count, 10));
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable int id) {
        if (id < 0) {
            throw new IllegalIdException();
        } else {
            return filmService.getFilmById(id);
        }
    }
}
