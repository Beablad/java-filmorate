package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        userStorage.getUserById(userId).addFriend(userStorage.getUserById(friendId));
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.getUserById(userId).deleteFriend(userStorage.getUserById(friendId));
    }

    public List<User> getMutualFriends(int userId, int otherUserId) {
        List<User> mutualFriends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        User otherUser  = userStorage.getUserById(otherUserId);
        for (User friend: user.getFriendList()){
            if (otherUser.getFriendList().contains(friend)){
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }
}
