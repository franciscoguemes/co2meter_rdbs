package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.model.CO2Status;
import com.franciscoguemes.samples.co2meter_rdbs.model.Sensor;
import com.franciscoguemes.samples.co2meter_rdbs.model.SensorState;
import com.franciscoguemes.samples.co2meter_rdbs.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = SensorsController.class)
class SensorsControllerIntegrationTest {


    public static final String SENSORS_URL = "/api/v1/sensors/{uuid}";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SensorService sensorService;

    @BeforeEach
    void setUp() {
    }


    @Test
    public void givenAnExistingSensorUUID_whenGetSensorStatus_thenReturnJsonObject() throws Exception {
        UUID uuid = UUID.randomUUID();
        Sensor sensor = new Sensor(new SensorState(CO2Status.OK, false));

        given(sensorService.getSensor(Mockito.any())).willReturn(Optional.of(sensor));

        mvc.perform(get(SENSORS_URL, uuid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("OK")));
    }


    @Test
    public void givenAnUnknownSensorUUID_whenGetSensorStatus_thenReturn404() throws Exception {
        UUID uuid = UUID.randomUUID();
        given(sensorService.getSensor(Mockito.any())).willReturn(Optional.empty());

        mvc.perform(get("/api/v1/sensors/{uuid}", uuid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}