package com.franciscoguemes.samples.co2meter_rdbs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MetricsDTO(@JsonProperty("maxLast30Days") int maxLast30Days,
                         @JsonProperty("avgLast30Days") int avgLast30Days) {
}
