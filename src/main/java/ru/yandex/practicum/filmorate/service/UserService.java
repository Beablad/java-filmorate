package ru.yandex.practicum.filmorate.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    public final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        user.addFriend(userStorage.getUserById(friendId));
        userStorage.getUserById(friendId).addFriend(user);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        user.deleteFriend(userStorage.getUserById(friendId));
        userStorage.getUserById(friendId).deleteFriend(user);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);
        for (Integer friend : user.getFriendList()) {
            if (otherUser.getFriendList().contains(friend)) {
                commonFriends.add(userStorage.getUserById(friend));
            }
        }
        return commonFriends;
    }

    public List<User> getFriendsListOfUser(int id) {
        List<User> friendList = new ArrayList<>();
        User user = userStorage.getUserById(id);
        for (int friendId: user.getFriendList()){
            friendList.add(userStorage.getUserById(friendId));
        }
        return friendList;
    }
}
