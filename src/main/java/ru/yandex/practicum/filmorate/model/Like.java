package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Like {

    @NonNull
    private long filmId;
    @NonNull
    private long userId;
}
