package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.dto.MeasurementDTO;
import com.franciscoguemes.samples.co2meter_rdbs.model.Measurement;
import com.franciscoguemes.samples.co2meter_rdbs.service.MeasurementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = MeasurementsController.class)
class MeasurementsControllerIntegrationTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private MeasurementService measurementService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void whenPostMeasurement_thenCreateMeasurment() throws Exception {

        UUID uuid = new UUID(0, 1);
        Measurement measurement = new Measurement(uuid, 2000, OffsetDateTime.now());
        MeasurementDTO measurementDTO = new MeasurementDTO(measurement.co2(), measurement.time());
        given(measurementService.addMeasurement(Mockito.any())).willReturn(measurement);


        mvc.perform(post("/api/v1/sensors/{uuid}/measurements", uuid).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(measurementDTO))).andExpect(status().isOk()).andExpect(jsonPath("$.co2", is(2000)));
        verify(measurementService, VerificationModeFactory.times(1)).addMeasurement(Mockito.any());
        reset(measurementService);
    }

    @Test
    public void givenMeasurements_whenGetMeasurements_thenReturnJsonArray() throws Exception {
        UUID uuid = new UUID(0, 1);
        OffsetDateTime time1 = OffsetDateTime.now();
        List<Measurement> allMeasurements = Arrays.asList(
                new Measurement(uuid, 2000, time1),
                new Measurement(uuid, 2000, OffsetDateTime.now().plusSeconds(1)),
                new Measurement(uuid, 2000, OffsetDateTime.now().plusSeconds(2))
        );

        given(measurementService.getMeasurementsOf(Mockito.any())).willReturn(allMeasurements);

        mvc.perform(get("/api/v1/sensors/{uuid}/measurements", uuid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].co2", is(2000)))
                .andExpect(jsonPath("$[0].time", is(time1.toZonedDateTime().toString())));

        reset(measurementService);
    }

}