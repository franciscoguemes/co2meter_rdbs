package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.dto.MeasurementDTO;
import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;
import com.franciscoguemes.samples.co2meter_rdbs.service.MeasurementService;
import io.micrometer.common.lang.NonNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("api/v1/sensors/{uuid}/measurements")
@RestController
public class MeasurementsController {

    private final MeasurementService measurementService;


    @Autowired
    public MeasurementsController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }


    @Operation(
            summary = "Store the measurement of a sensor by its UUID",
            description = "Store the measurement of the sensor (UUID) in the service"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The measurement was stored",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementDTO.class))}
            ),
            @ApiResponse(responseCode = "400", description = "There given UUID is not valid",
                    content = @Content),
    })
    @PostMapping()
    public void addMeasurement(@PathVariable("uuid") UUID uuid, @NonNull @RequestBody MeasurementDTO measurementDTO) {
        measurementService.addMeasurement(new Measurement(uuid, measurementDTO.co2(), measurementDTO.time()));
    }


    @Operation(
            summary = "Get the measurements of a sensor by its UUID",
            description = "Get all measurements of the sensor whose UUID is provided"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements of the given sensor were found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementDTO.class))}),
            @ApiResponse(responseCode = "400", description = "There given UUID is not valid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "There are no measurments for that UUID (Sensor)",
                    content = @Content)})
    @GetMapping
    public List<MeasurementDTO> getMeasurementsOf(@PathVariable("uuid") UUID uuid) {
        List<Measurement> measurements = measurementService.getMeasurementsOf(uuid);
        if (measurements.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return measurements.stream().map(measurement -> new MeasurementDTO(measurement.co2(), measurement.time())).collect(Collectors.toList());
    }
}
