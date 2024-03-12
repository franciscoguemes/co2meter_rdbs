package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.dto.MeasurementDTO;
import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;
import com.franciscoguemes.samples.co2meter_rdbs.service.MeasurementService;
import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("api/v1/sensors/{uuid}/measurements")
@RestController
public class MeasurementController {

    private final MeasurementService measurementService;


    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }


    @PostMapping()
    public void addMeasurement(@PathVariable("uuid") UUID uuid, @NonNull @RequestBody MeasurementDTO measurementDTO) {
        measurementService.addMeasurement(new Measurement(uuid, measurementDTO.co2(), measurementDTO.time()));
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurementsOf(@PathVariable("uuid") UUID uuid) {
        List<Measurement> measurements = measurementService.getMeasurementsOf(uuid);
        if (measurements.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return measurements.stream().map(measurement -> new MeasurementDTO(measurement.co2(), measurement.time())).collect(Collectors.toList());
    }
}
