package com.franciscoguemes.samples.co2meter_rdbs.service;

import com.franciscoguemes.samples.co2meter_rdbs.dao.MeasurementDao;
import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MeasurementService {

    private final MeasurementDao measurementDao;

    @Autowired
    public MeasurementService(@Qualifier("fakeDao") MeasurementDao measurementDao) {
        this.measurementDao = measurementDao;
    }

    public int addMeasurement(Measurement measurement) {
        return measurementDao.insertMeasurement(measurement);
    }

    public List<Measurement> getMeasurementsOf(UUID uuid) {
        return measurementDao.getAllMeasurements(uuid);
    }
}
