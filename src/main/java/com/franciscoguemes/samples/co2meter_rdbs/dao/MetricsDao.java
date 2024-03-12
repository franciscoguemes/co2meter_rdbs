package com.franciscoguemes.samples.co2meter_rdbs.dao;

import com.franciscoguemes.samples.co2meter_rdbs.model.Metrics;

import java.util.Optional;
import java.util.UUID;

public interface MetricsDao {

    Optional<Metrics> getMetrics(UUID uuid);

}
