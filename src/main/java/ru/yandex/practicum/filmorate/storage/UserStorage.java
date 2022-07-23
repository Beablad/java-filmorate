package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User deleteUser(int id);

    User updateUser(User user);

    List<User> getUserList();

    User getUserById (int userId);
}
