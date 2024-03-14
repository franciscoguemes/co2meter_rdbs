package com.franciscoguemes.samples.co2meter_rdbs.service;

import com.franciscoguemes.samples.co2meter_rdbs.dao.MetricsDao;
import com.franciscoguemes.samples.co2meter_rdbs.model.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MetricsService {


    private final MetricsDao metricsDao;

    @Autowired
    public MetricsService(@Qualifier("${app.das}") MetricsDao metricsDao) {
        this.metricsDao = metricsDao;
    }


    public Optional<Metrics> getMetrics(UUID uuid) {
        return this.metricsDao.getMetrics(uuid);
    }

}
