package com.franciscoguemes.samples.co2meter_rdbs.dao;

import com.franciscoguemes.samples.co2meter_rdbs.model.CO2Status;
import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;
import com.franciscoguemes.samples.co2meter_rdbs.model.Sensor;
import com.franciscoguemes.samples.co2meter_rdbs.model.SensorState;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractDataAccessService implements MeasurementDao, MetricsDao, SensorDao {

    @Override
    public Optional<Sensor> getSensor(UUID uuid) {
        List<Measurement> measurements = this.getAllMeasurements(uuid);

        if (measurements.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new Sensor(this.calculateSensorState(measurements)));
    }

    protected SensorState calculateSensorState(List<Measurement> measurements) {
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

    protected SensorState calculateInitialSensorState(Measurement m) {
        return new SensorState(CO2Status.fromC02Level(m.co2()), false);
    }

    protected SensorState calculateFromPreviousStates(SensorState s0, SensorState s1, Measurement m) {
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
