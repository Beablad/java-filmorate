package ru.yandex.practicum.filmorate.model;

import com.google.gson.annotations.Expose;
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
    private HashSet<Integer> friendList = new HashSet<>();

    public void addFriend(User user) {
        friendList.add(user.getId());
    }

    public void deleteFriend(User user) {
        friendList.remove(user.getId());
    }
}
