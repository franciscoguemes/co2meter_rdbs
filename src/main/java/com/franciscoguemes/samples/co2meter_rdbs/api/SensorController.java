package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.dto.SensorDTO;
import com.franciscoguemes.samples.co2meter_rdbs.dto.Status;
import com.franciscoguemes.samples.co2meter_rdbs.model.Sensor;
import com.franciscoguemes.samples.co2meter_rdbs.service.SensorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/v1/sensors/{uuid}")
@RestController
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public SensorDTO getSensor(@PathVariable("uuid") UUID uuid) {
        Optional<Sensor> sensor = this.sensorService.getSensor(uuid);
        Sensor s = sensor.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
        return new SensorDTO(Status.ofSensorSate(s.state()));
    }

}
