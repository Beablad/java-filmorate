package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {

    private long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private long duration;
    private LinkedHashSet<Genre> genres;
    private MpaRating mpa;

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull long duration, LinkedHashSet<Genre> genres, MpaRating mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
    }

    private HashSet<Like> likeList = new HashSet<>();

    public void addLike(Like like) {
        likeList.add(like);
    }

    public void deleteLike(User user) {
        likeList.remove(user);
    }
}
