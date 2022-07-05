package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.LocalDateTimeAdapter;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    Map<Integer, User> userList = new HashMap();
    int userId = 1;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTimeAdapter()).create();


    private int getUserId() {
        return userId++;
    }

    @GetMapping
    public List<User> getUserList() {
        log.info("Получен GET запрос");
        return new ArrayList<>(userList.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Получен POST запрос");
        if (validate(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(getUserId());
            userList.put(user.getId(), user);
            log.info("Пользователь добавлен");
        }
        return user;
    }

    @PutMapping
    public User updateOrAddUser(@RequestBody User user) {
        log.info("Получен PUT запрос");
        if (userList.get(user.getId()) != null && validate(user)) {
            userList.put(user.getId(), user);
            log.info("Пользователь обновлен");
        } else if (validate(user)) {
            userList.put(user.getId(), user);
            log.info("Пользователь добавлен");
        }
        return user;
    }

    private boolean validate(User user) {
        boolean checkEmail = user.getEmail().contains("@") && !user.getEmail().isBlank();
        boolean checkBirthday = user.getBirthday().isBefore(LocalDate.now());
        boolean checkLogin = !user.getLogin().contains(" ") && !user.getLogin().isBlank();
        if (user.getId() < 0) {
            log.warn("Ошибка валидации");
            throw new IllegalIdException();
        } else if (checkEmail && checkBirthday && checkLogin) {
            return true;
        } else {
            log.warn("Ошибка валидации");
            throw new ValidationException();
        }
    }
}

