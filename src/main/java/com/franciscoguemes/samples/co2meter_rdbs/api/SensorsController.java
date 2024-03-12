package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.dto.MetricsDTO;
import com.franciscoguemes.samples.co2meter_rdbs.dto.SensorDTO;
import com.franciscoguemes.samples.co2meter_rdbs.dto.Status;
import com.franciscoguemes.samples.co2meter_rdbs.model.Sensor;
import com.franciscoguemes.samples.co2meter_rdbs.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class SensorsController {

    private final SensorService sensorService;

    public SensorsController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Operation(
            summary = "Get the sensor by its UUID",
            description = "Get information of the sensor whose UUID is provided is the URL"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information of the given sensor were found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MetricsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "There given sensor (UUID) is not valid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "There given sensor (UUID) does not exists",
                    content = @Content)})
    @GetMapping
    public SensorDTO getSensor(@PathVariable("uuid") UUID uuid) {
        Optional<Sensor> sensor = this.sensorService.getSensor(uuid);
        Sensor s = sensor.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
        return new SensorDTO(Status.ofSensorSate(s.state()));
    }

}
