package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User deleteUser (int id)  {
        return userStorage.deleteUser(id);
    }
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        return friendStorage.getCommonFriends(userId, otherUserId);
    }

    public List<User> getFriendsListOfUser(int id) {
        return friendStorage.getFriendsOfUser(id);
    }
}
