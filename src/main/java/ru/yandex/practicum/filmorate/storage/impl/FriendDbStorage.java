package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.control.CodeGenerationHint;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@AllArgsConstructor
@Primary
@Component
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Override
    public void addFriend(int userId, int friendId) {
        if (friendId<=0){
            throw new IllegalIdException();
        }
        String sql = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = userDbStorage.getUserById(userId);
        user.deleteFriend(friendId);
        String sql = "DELETE FROM FRIENDSHIP where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
        public List<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT u.*"
                + " FROM users AS u"
                + " WHERE u.user_id IN ("
                + "   SELECT friend_id AS user_id FROM friendship WHERE user_id = ?"
                + "   UNION"
                + "   SELECT user_id AS user_id FROM friendship WHERE friend_id = ?"
                + " )"
                + "AND u.user_id IN ("
                + "   SELECT friend_id AS user_id FROM friendship WHERE user_id = ?"
                + "   UNION"
                + "   SELECT user_id AS user_id FROM friendship WHERE friend_id = ?"
                + " )";
        return jdbcTemplate.query(sql, userDbStorage::mapToRowUser, userId, userId, friendId, friendId);
    }

    @Override
    public List<User> getFriendsOfUser(int userId) {
        String sql = "SELECT u.*"
                + " FROM users AS u"
                + " WHERE u.user_id IN ("
                + "   SELECT friend_id AS user_id FROM friendship WHERE user_id = ?"
                + " )";
        return jdbcTemplate.query(sql, userDbStorage::mapToRowUser, userId);
    }
}
