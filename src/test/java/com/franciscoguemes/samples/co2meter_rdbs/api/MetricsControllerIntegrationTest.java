package com.franciscoguemes.samples.co2meter_rdbs.api;

import com.franciscoguemes.samples.co2meter_rdbs.model.Metrics;
import com.franciscoguemes.samples.co2meter_rdbs.service.MetricsService;
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

@WebMvcTest(value = MetricsController.class)
class MetricsControllerIntegrationTest {

    public static final String METRICS_URL = "/api/v1/sensors/{uuid}/metrics";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MetricsService metricsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void givenAnExistingSensorUUID_whenGetSensorMetrics_thenReturnJsonObject() throws Exception {
        UUID uuid = UUID.randomUUID();

        Metrics metrics = new Metrics(3000, 1700);

        given(metricsService.getMetrics(Mockito.any())).willReturn(Optional.of(metrics));
        mvc.perform(get(METRICS_URL, uuid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxLast30Days", is(3000)))
                .andExpect(jsonPath("$.avgLast30Days", is(1700)));

    }

    @Test
    public void givenAnUnknownSensorUUID_whenGetSensorMetrics_thenReturn404() throws Exception {
        UUID uuid = UUID.randomUUID();

        given(metricsService.getMetrics(Mockito.any())).willReturn(Optional.empty());
        mvc.perform(get("/api/v1/sensors/{uuid}/metrics", uuid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


}