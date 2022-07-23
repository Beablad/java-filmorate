package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import com.google.gson.Gson;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    @Test
    void addUser() throws Exception {
        User user = new User("a", "b", "email@mail.ru", LocalDate.of(2000, 7, 1));
        mockMvc.perform(post("/users")
                        .content(gson.toJson(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addUserWithoutLogin() throws Exception {
        User user = new User("a", "", "email@mail.ru", LocalDate.of(2000, 7, 1));
        mockMvc.perform(post("/users")
                        .content(gson.toJson(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addUserWithIncorrectBirthday() throws Exception {
        User user = new User("name", "", "email@mail.ru", LocalDate.MAX);
        mockMvc.perform(post("/users")
                        .content(gson.toJson(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addUserWithoutName() throws Exception {
        User user = new User("", "login", "email@mail.ru", LocalDate.EPOCH);
        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user))
                )
                .andExpect(status().isOk()).andReturn();
        user.setName(user.getLogin());
        user.setId(2);
        assertEquals(gson.toJson(user), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void addUserWithIncorrectEmail() throws Exception {
        User user = new User("", "login", "emailmail.ru", LocalDate.EPOCH);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUsers() throws Exception {
        User user = new User("a", "b", "a@a", LocalDate.of(2000, 7, 1));
        user.setId(1);
        mockMvc.perform(post("/users").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }
}