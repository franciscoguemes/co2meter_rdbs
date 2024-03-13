package com.franciscoguemes.samples.co2meter_rdbs.dao;

import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;
import com.franciscoguemes.samples.co2meter_rdbs.model.Metrics;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fakeDao")
public class FakeDataAccessService extends AbstractDataAccessService {

    public static final int THIRTY = 30;
    private static final List<Measurement> DB = new ArrayList<>();

    @Override
    public int insertMeasurement(Measurement measurement) {
        DB.add(measurement);
        return 1;
    }

    @Override
    public List<Measurement> getAllMeasurements(UUID uuid) {
        return DB.stream()
                .filter(m -> m.uuid().equals(uuid))
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Measurement> getLast30DaysMeasurements(UUID uuid) {
        Date xDaysAgo = Date.from(Instant.now().minus(Duration.ofDays(THIRTY)));

        return DB.stream()
                .filter(m -> m.uuid().equals(uuid) && m.time().after(xDaysAgo))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Metrics> getMetrics(UUID uuid) {
        List<Measurement> measurements = this.getLast30DaysMeasurements(uuid);
        if (measurements.isEmpty()) {
            return Optional.empty();
        }

        int max = -1;
        int sumTotal = 0;
        Iterator<Measurement> it = measurements.iterator();
        while (it.hasNext()) {
            Measurement m = it.next();
            max = m.co2() > max ? m.co2() : max;
            sumTotal += m.co2();
        }
        int avg = sumTotal / measurements.size();

        return Optional.of(new Metrics(max, avg));
    }


}
