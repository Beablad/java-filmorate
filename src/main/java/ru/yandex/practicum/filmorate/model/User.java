package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    @NonNull String name;
    @NonNull String login;
    @NonNull private String email;
    @NonNull LocalDate birthday;
}
