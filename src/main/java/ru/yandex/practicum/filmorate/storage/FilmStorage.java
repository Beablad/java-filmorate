package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film deleteFilm(int id);

    Film updateFilm(Film film);

    List<Film> getFilmList();

    Film getFilmById(int id);
}
