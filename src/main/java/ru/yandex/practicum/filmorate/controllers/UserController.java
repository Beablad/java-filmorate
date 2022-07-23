package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUserList() {
        return userService.userStorage.getUserList();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.userStorage.addUser(user);
    }

    @PutMapping
    public User updateOrAddUser(@RequestBody User user) {
        return userService.userStorage.updateUser(user);
    }

    @DeleteMapping (value = "/{id}")
    public User deleteUser (@PathVariable int id)  {
        return userService.userStorage.deleteUser(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        return userService.getFriendsListOfUser(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.userStorage.getUserById(id);
    }
}

