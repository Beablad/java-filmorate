package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaRatingDb {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_MPA_BY_ID =
            "SELECT * " +
                    "FROM MPA_RATING " +
                    "WHERE MPA_RATING_ID = ?;";

    public MpaRating getMpaById(Long mpaId) {
        SqlRowSet mpaRatingAsRowSet = jdbcTemplate.queryForRowSet(FIND_MPA_BY_ID, mpaId);
        if (mpaRatingAsRowSet.next()) {
            return MpaRating.builder()
                    .id(mpaRatingAsRowSet.getLong("mpa_rating_id"))
                    .name(mpaRatingAsRowSet.getString("mpa_rating_name"))
                    .build();
        }
        throw new IllegalArgumentException("Mpa with this id is not exist");
    }

    public List<MpaRating> getMpaList() {
        String sql = "SELECT * FROM MPA_RATING";
        return jdbcTemplate.query(sql, this::mapForRowMpa);
    }

    private MpaRating mapForRowMpa(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(rs.getInt("mpa_rating_id"))
                .name(rs.getString("mpa_rating_name"))
                .build();
    }
}