package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    public void addFriend(int userId, int friendId);

    public void deleteFriend(int userId, int friendId);

    public List<User> getCommonFriends(int userId, int friendId);

    public List<User> getFriendsOfUser(int userId);
}
