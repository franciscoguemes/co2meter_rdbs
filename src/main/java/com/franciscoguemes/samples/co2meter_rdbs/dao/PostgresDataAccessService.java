package com.franciscoguemes.samples.co2meter_rdbs.dao;

import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;
import com.franciscoguemes.samples.co2meter_rdbs.model.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgresDao")
public class PostgresDataAccessService extends AbstractDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgresDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertMeasurement(Measurement measurement) {
        String sql = "INSERT INTO measurement VALUES(?,?,?)";
        return this.jdbcTemplate.update(sql, measurement.uuid(), measurement.time(), measurement.co2());
    }

    @Override
    public List<Measurement> getAllMeasurements(UUID uuid) {
        String sql = """
                SELECT *
                FROM measurement
                WHERE uuid=?
                ORDER BY "time" ASC;
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, i) -> {
                    int co2 = resultSet.getInt("co2");
                    OffsetDateTime time = resultSet.getObject("time", OffsetDateTime.class);
                    String uuid1 = resultSet.getString("uuid");
                    return new Measurement(
                            UUID.fromString(uuid1),
                            co2,
                            time
                    );
                },
                uuid);
    }

    @Override
    public Optional<Metrics> getMetrics(UUID uuid) {

        String sql = """
                SELECT MAX(co2) AS maxLast30Days ,AVG(co2) AS avgLast30Days
                FROM public.measurement
                WHERE uuid=? AND time > (CURRENT_TIMESTAMP - INTERVAL '30 days');
                """;

        Metrics metrics = jdbcTemplate.queryForObject(
                sql,
                (resultSet, i) -> {
                    int maxLast30Days = resultSet.getInt("maxLast30Days");
                    int avgLast30Days = Math.toIntExact(Math.round(resultSet.getDouble("avgLast30Days")));
                    return new Metrics(
                            maxLast30Days,
                            avgLast30Days
                    );
                },
                uuid);

        return Optional.ofNullable(metrics);
    }

}
