package com.franciscoguemes.samples.co2meter_rdbs.dto;

import com.franciscoguemes.samples.co2meter_rdbs.model.CO2Status;
import com.franciscoguemes.samples.co2meter_rdbs.model.SensorState;

public enum Status {

    OK,
    WARN,
    ALERT;


    public static Status ofSensorSate(SensorState sensorState) {
        if (sensorState.alert()) {
            return Status.ALERT;
        }

        if (sensorState.co2status().equals(CO2Status.WARN)) {
            return Status.WARN;
        }

        return Status.OK;
    }

//    private static final int CO2_LIMIT=2000;

//    public static Status fromCurrentCO2Level(int co2, boolean alertTriggered){
//        if(alertTriggered){
//            return Status.ALERT;
//        }
//        if(co2 <CO2_LIMIT){
//            return Status.OK;
//        }
//        return  Status.WARN;
//    }

//    public static Status fromC02Level(int co2){
//        if(co2 <CO2_LIMIT){
//            return Status.OK;
//        }
//        return  Status.WARN;
//    }
//
//    public static Status fromPreviousStatus(Status s0, Status s1, int actual){
//        if(alertTriggered){
//
//        }
//        if(actual<CO2_LIMIT){
//            return Status.OK;
//        }else{
//
//        }
//
//    }

}
