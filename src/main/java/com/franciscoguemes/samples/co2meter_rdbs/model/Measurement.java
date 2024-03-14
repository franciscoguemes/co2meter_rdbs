package com.franciscoguemes.samples.co2meter_rdbs.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Measurement(UUID uuid, int co2, OffsetDateTime time) implements Comparable<Measurement> {

    @Override
    public int compareTo(Measurement o) {
        if (this.time.equals(o.time)) {
            return 0;
        }
        if (this.time.toInstant().isBefore(o.time.toInstant())) {
            return -1;
        }
        return 1;
    }
}
