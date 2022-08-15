package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.impl.MpaRatingDb;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping ("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<MpaRating> getMpaList () {
        return mpaService.getMpaList();
    }

    @GetMapping (value = "/{id}")
    public MpaRating getMpaById (@PathVariable long id) {
        return mpaService.getMpaById(id);
    }
}
