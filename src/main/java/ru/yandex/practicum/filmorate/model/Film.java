package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

@Data
public class Film {

    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private long duration;
    private HashSet<User> likeList = new HashSet<>();

    public void addLike(User user) {
        likeList.add(user);
    }

    public void deleteLike(User user) {
        likeList.remove(user);
    }
}
