C02 Meter (RDBS)
========================================
A Spring Boot based Java service that acts as a centralized service capable of collecting data from hundreds of
thousands of sensors and alert if the CO2 concentrations reach critical levels.

# Table of contents

- [Introduction](#introduction)
- [Functional requirments](#functional-requirements)
- [API Specifications](#api-specifications)

# Introduction

Carbon Dioxide (CO2) is all around us and we are constantly expelling it, but in high concentration CO2 can be harmful
or even lethal. CO2 Levels between 2000 and 5000 ppm are associated with headaches, sleepiness poor concentration, loss
of attention, increased heart rate and slight nausea may also be present.

The company AirQuality Inc. has hundreds of sensors distributed all across the globe. Each of these sensors measures the
amount of CO2 in a specific location of the planet. AirQuality Inc needs a centralize solution to store and analyze the
enormous amount of data that is being collected by its network of sensors (IoT).

# Functional requirements

The service should be able to receive measurements from each sensor (hundreds of thousands) at the rate of 1 per minute

## Alerting

Initially sensor status is OK. If the CO2 level equals or exceeds 2000 ppm, the sensor status should be set to WARN.
If the service receives 3 or more consecutive measurements higher than 2000, the sensor status should be set to ALERT.

When the sensor reaches status ALERT, it stays in this state until it receives 3 consecutive measurements lower than
2000; then it moves to OK.

## Metrics

The service should provide the following metrics about each sensor:
◦ Average CO2 level for the last 30 days
◦ Maximum CO2 Level in the last 30 dayAPI

# API Specifications

You can see the [api-docs in JSON format](http://localhost:8080/api/v1/api-docs) or check
the [swagger-ui](http://localhost:8080/api/v1/swagger-ui/index.html)

## Collect sensor measurements

```
POST /api/v1/sensors/{uuid}/measurements
{
   "co2" : 2000,
   "time" : "2019-02-01T18:55:47+00:00"
}
```

## Sensor status

```
GET /api/v1/sensors/{uuid}
```

Response:

```
{
   "status" : "OK" // Possible status OK,WARN,ALERT
}
```   

## Sensor metrics

```
GET /api/v1/sensors/{uuid}/metrics
```

Response:

```
{
   "maxLast30Days" : 1200,
   "avgLast30Days" : 900
}
```

---

Project generated
with [Spring initializr](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=3.2.3&packaging=jar&jvmVersion=21&groupId=com.franciscoguemes.samples&artifactId=co2meter_rdbs&name=co2meter_rdbs&description=A%20Spring%20Boot%20based%20Java%20service%20that%20acts%20as%20a%20centralized%20service%20capable%20of%20collecting%20data%20from%20hundreds%20of%20%20thousands%20of%20sensors%20and%20alert%20if%20the%20CO2%20concentrations%20reach%20critical%20levels.&packageName=com.franciscoguemes.samples.co2meter_rdbs&dependencies=web,flyway,postgresql)