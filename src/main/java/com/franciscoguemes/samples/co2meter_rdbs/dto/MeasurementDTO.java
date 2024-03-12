package com.franciscoguemes.samples.co2meter_rdbs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.lang.NonNull;

import java.util.Date;

public record MeasurementDTO(@JsonProperty("co2") int co2, @NonNull @JsonProperty("time") Date time) {
}
