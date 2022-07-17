package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userStorage.getUserById(userId));
        log.info("Добавлен лайку фильму " + film);
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(userStorage.getUserById(userId));
        log.info("Удален лайк у фильма " + film);
        return film;
    }

    public List<Film> getMostLikesFilm(Optional<Integer> count) {
        return filmStorage.getFilmList().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikeList().size()).reversed())
                .limit(count.orElse(10))
                .collect(Collectors.toList());
    }
}
