package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User addUser(User user);

    public User deleteUser(User user);

    public User updateUser(User user);

    public List<User> getUserList();

    public User getUserById (int userId);
}