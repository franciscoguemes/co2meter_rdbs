package com.franciscoguemes.samples.co2meter_rdbs.dao;

import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;

import java.util.List;
import java.util.UUID;

public interface MeasurementDao {
    Measurement insertMeasurement(Measurement measurement);

    List<Measurement> getAllMeasurements(UUID uuid);

}
