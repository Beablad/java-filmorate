package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.GenreDb;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    GenreDb genreDb;

    public List<Genre> getGenres () {
        return genreDb.getGenres();
    }

    public Genre getGenreById (long id) {
        if (id<=0) {
            throw new IllegalIdException();
        }
        return genreDb.getGenreById(id);
    }
}
