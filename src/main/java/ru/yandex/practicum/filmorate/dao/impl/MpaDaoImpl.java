package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MPA> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.queryForStream(sql, this::mapRowToMpa).collect(Collectors.toList());
    }

    @Override
    public MPA getMpaById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        } catch (DataAccessException e) {
            String errorMessage = String.format("В базе данных отсутствует MPA с id=%d", id);
            throw new NotFoundException(errorMessage);
        }
    }

    public MPA mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}
