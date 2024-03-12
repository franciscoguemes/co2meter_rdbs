package com.franciscoguemes.samples.co2meter_rdbs.dao;

import com.franciscoguemes.samples.co2meter_rdbs.model.*;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fakeDao")
public class FakeMeasurementDataAccessService implements MeasurementDao, MetricsDao, SensorDao {

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

    @Override
    public List<Measurement> getLast30DaysMeasurements(UUID uuid) {
        Date xDaysAgo = Date.from(Instant.now().minus(Duration.ofDays(THIRTY)));

        return DB.stream()
                .filter(m -> m.uuid().equals(uuid) && m.time().after(xDaysAgo))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Measurement> getLatest4Measurements(UUID uuid) {
        ArrayList<Measurement> latestFour = new ArrayList<>(4);

        List<Measurement> measurements = DB.stream()
                .filter(m -> m.uuid().equals(uuid))
                .sorted()
                .collect(Collectors.toList());

        measurements = measurements.reversed().stream().limit(4).collect(Collectors.toList());
        latestFour.addAll(measurements.reversed().stream().toList());

        return latestFour;
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

    @Override
    public Optional<Sensor> getSensor(UUID uuid) {
        List<Measurement> measurements = this.getAllMeasurements(uuid);

        if (measurements.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new Sensor(this.calculateSensorState(measurements)));
    }

    private SensorState calculateSensorState(List<Measurement> measurements) {
        if (measurements.size() < 3) {
            return this.calculateInitialSensorState(measurements.getLast());
        }

        Iterator<Measurement> it = measurements.iterator();
        SensorState s0 = this.calculateInitialSensorState(it.next());
        SensorState s1 = this.calculateInitialSensorState(it.next());
        SensorState current = this.calculateFromPreviousStates(s0, s1, it.next());
        while (it.hasNext()) {
            s0 = s1;
            s1 = current;
            current = this.calculateFromPreviousStates(s0, s1, it.next());
        }
        return current;
    }

    private SensorState calculateInitialSensorState(Measurement m) {
        return new SensorState(CO2Status.fromC02Level(m.co2()), false);
    }

    private SensorState calculateFromPreviousStates(SensorState s0, SensorState s1, Measurement m) {
        if (s0.alert()) {
            if (s1.alert()) {
                CO2Status status = CO2Status.fromC02Level(m.co2());
                if (s0.co2status().equals(CO2Status.OK) && s1.co2status().equals(CO2Status.OK)) {
                    if (status.equals(CO2Status.OK)) {
                        return new SensorState(status, false);
                    }
                }
                return new SensorState(status, true);
            } else {
                return new SensorState(CO2Status.fromC02Level(m.co2()), false);
            }
        } else {
            if (s1.alert()) {
                return new SensorState(CO2Status.fromC02Level(m.co2()), true);
            } else {
                CO2Status status = CO2Status.fromC02Level(m.co2());
                if (s0.co2status().equals(CO2Status.WARN) && s1.co2status().equals(CO2Status.WARN)) {
                    if (status.equals(CO2Status.WARN)) {
                        return new SensorState(status, true);
                    }
                }
                return new SensorState(status, false);
            }
        }
    }

}
