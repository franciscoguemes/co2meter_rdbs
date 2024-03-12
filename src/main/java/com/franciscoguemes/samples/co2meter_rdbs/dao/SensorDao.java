package com.franciscoguemes.samples.co2meter_rdbs.dao;

import com.franciscoguemes.samples.co2meter_rdbs.model.Sensor;

import java.util.Optional;
import java.util.UUID;

public interface SensorDao {

    Optional<Sensor> getSensor(UUID uuid);

}
