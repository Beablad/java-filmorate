package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.util.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    /*Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    @Test
    void addFilm() throws Exception {
        Film film = new Film("film", "descr", LocalDate.EPOCH, 120);
        mockMvc.perform(post("/films")
                        .content(gson.toJson(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void addFilmWithoutName() throws Exception {
        Film film = new Film("", "descr", LocalDate.EPOCH, 120);
        mockMvc.perform(post("/films")
                        .content(gson.toJson(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addFilmWithLongDescription() throws Exception {
        String longDescription = "asdl;adkjsal;fjsdfl;kasjdflakdswjf;lakdsjfalkadjfla;ksdjfla;skdjfalskdjfalkdjfalsk" +
                "dfjasldkfjalsdkfjasldkfjlkn lkajvns;lkadfnklfjdvn;slakdvnakljfnaskld;jfnkljfvnaskl;djfnasldkjvnklsa" +
                ";djnvsdkla;jfnk;jcsnvk;xcjnkl;jzsndfk;jxcnvkl;xznvlzk;nvasd;kljnvkxcnvxz";
        Film film = new Film("film", longDescription, LocalDate.EPOCH, 120);
        mockMvc.perform(post("/films")
                        .content(gson.toJson(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addFilmWithZeroDuration() throws Exception {
        Film film = new Film("film", "descr", LocalDate.EPOCH, 0);
        mockMvc.perform(post("/films")
                        .content(gson.toJson(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addOldFilm() throws Exception {
        Film film = new Film("film", "descr", LocalDate.MIN, 120);
        mockMvc.perform(post("/films")
                        .content(gson.toJson(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getFilms() throws Exception {
        Film film = new Film("film", "descr", LocalDate.EPOCH, 120);
        mockMvc.perform(post("/films")
                .content(gson.toJson(film))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/films")).andExpect(status().isOk());
    }*/
}