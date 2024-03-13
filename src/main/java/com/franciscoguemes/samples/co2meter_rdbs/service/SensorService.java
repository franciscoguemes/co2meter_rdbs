package com.franciscoguemes.samples.co2meter_rdbs.service;

import com.franciscoguemes.samples.co2meter_rdbs.dao.SensorDao;
import com.franciscoguemes.samples.co2meter_rdbs.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SensorService {

    private final SensorDao sensorDao;

    @Autowired
    public SensorService(@Qualifier("postgresDao") SensorDao sensorDao) {
        this.sensorDao = sensorDao;
    }


    public Optional<Sensor> getSensor(UUID uuid) {
        return sensorDao.getSensor(uuid);
    }

}
