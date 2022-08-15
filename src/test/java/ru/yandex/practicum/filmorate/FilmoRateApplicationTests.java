package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.GenreDb;
import ru.yandex.practicum.filmorate.storage.impl.MpaRatingDb;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreDb genreDb;
    private final MpaRatingDb mpaRatingDb;

    @Test
    public void testFindUserById() {
        userStorage.addUser(new User("test", "test", "test@mail.ru", LocalDate.EPOCH));
        User user = userStorage.getUserById(1);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testFindFilmById() {
        filmStorage.addFilm(new Film("test", "test", LocalDate.EPOCH, 120, null,
                MpaRating.builder()
                        .id(1)
                        .build()));
        Film film = filmStorage.getFilmById(1);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testFindGenreById() {
        Genre genre = genreDb.getGenreById(1L);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testFindMpaById() {
        MpaRating mpaRating = mpaRatingDb.getMpaById(1L);
        assertThat(mpaRating).hasFieldOrPropertyWithValue("id", 1L).hasFieldOrPropertyWithValue("name", "G");
    }
}
