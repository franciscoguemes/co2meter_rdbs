package com.franciscoguemes.samples.co2meter_rdbs.model;

public enum CO2Status {


    OK,
    WARN;

    private static final int CO2_LIMIT = 2000;

    public static CO2Status fromC02Level(int co2) {
        if (co2 < CO2_LIMIT) {
            return CO2Status.OK;
        }
        return CO2Status.WARN;
    }

}
