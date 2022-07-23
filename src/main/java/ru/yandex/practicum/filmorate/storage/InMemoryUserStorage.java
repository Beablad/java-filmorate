package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> userList = new HashMap();
    private int userId = 1;

    @Override
    public User addUser(User user) {
        if (validate(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(getUserId());
            userList.put(user.getId(), user);
            log.info("Пользователь добавлен.");
        }
        return user;
    }

    @Override
    public User deleteUser(int id) {
        userList.remove(getUserById(id).getId());
        log.info("Пользователь удален.");
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        if (validate(user)){
            userList.put(user.getId(), user);
            log.info("Пользователь добавлен/обновлен.");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User getUserById (int userId) {
        if (userId > 0){
            return userList.get(userId);
        } else {
            throw new IllegalIdException();
        }
    }

    private boolean validate(User user) {
        boolean checkEmail = user.getEmail().contains("@") && !user.getEmail().isBlank();
        boolean checkBirthday = user.getBirthday().isBefore(LocalDate.now());
        boolean checkLogin = !user.getLogin().contains(" ") && !user.getLogin().isBlank();
        if (user.getId() < 0) {
            throw new IllegalIdException();
        } else if (checkEmail && checkBirthday && checkLogin) {
            return true;
        } else {
            throw new ValidationException();
        }
    }

    private int getUserId() {
        return userId++;
    }
}
