package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;


    public Film addFilm(Film film) {
        log.info("Получен POST запрос");
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        return film;
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public void addLike(int filmId, int userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getMostLikesFilm(int count) {
        return filmStorage.getFilmList().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikeList().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilmList() {
        return filmStorage.getFilmList();
    }
}
