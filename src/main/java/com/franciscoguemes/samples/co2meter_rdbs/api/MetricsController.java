package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.dto.MetricsDTO;
import com.franciscoguemes.samples.co2meter_rdbs.model.Metrics;
import com.franciscoguemes.samples.co2meter_rdbs.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/v1/sensors/{uuid}/metrics")
@RestController
public class MetricsController {

    private final MetricsService metricsService;

    @Autowired
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public MetricsDTO getMetrics(@PathVariable("uuid") UUID uuid) {
        Optional<Metrics> metrics = this.metricsService.getMetrics(uuid);
        Metrics m = metrics.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
        return new MetricsDTO(m.maxLast30Days(), m.avgLast30Days());
    }
}
