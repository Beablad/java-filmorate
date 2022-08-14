package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
@Data
@Builder
public class MpaRating {

    private long id;
    private String name;
}
