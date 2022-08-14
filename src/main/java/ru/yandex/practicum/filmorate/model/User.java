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
    private HashSet<Friendship> friendList = new HashSet<>();

    public void addFriend(Friendship friendship) {
        friendList.add(friendship);
    }

    public void deleteFriend(int friendId) {
        for (Friendship friendship: friendList){
            if (friendship.getFriendId() == friendId){
                friendList.remove(friendship);
                break;
            }
        }
    }
}
