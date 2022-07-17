package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;

@Data
public class User {

    private int id;
    @NonNull
    private String name;
    @NonNull
    private String login;
    @NonNull
    private String email;
    @NonNull
    private LocalDate birthday;
    @NonNull
    private HashSet<User> friendList;

    public void addFriend(User user) {
        friendList.add(user);
    }

    public void deleteFriend(User user) {
        friendList.remove(user);
    }
}
