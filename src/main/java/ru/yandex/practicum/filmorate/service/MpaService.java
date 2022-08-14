package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IllegalIdException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.impl.MpaRatingDb;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {

    MpaRatingDb mpaRatingDb;

    public List<MpaRating> getMpaList() {
        return mpaRatingDb.getMpaList();
    }

    public MpaRating getMpaById (long id) {
        if (id<=0) {
            throw new IllegalIdException();
        }
        return mpaRatingDb.getMpaById(id);
    }
}
