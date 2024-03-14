Setup
==========================================================================

# Prerequisites

To run the project you need to install

- Java 21
- Gradle 8.6
- docker 25.0.4

## Running the application

To run the application in the command line is as simple as manually spin the DB container (see in the next section)
and once the container is up and running, execute the application using gradle:

```shell
gradlew run
```

During the startup of the application, it will run some migration scripts using Flyway in order to setup the DB in
the right version. Additionally it also pre-load some sample data so the developer can start using the API.

## Postgres DB

You can spin manually the docker container with the following command:

```shell
docker run --rm --name co2meter-postgres \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=co2meter \
  -d -p 5432:5432 postgres:alpine
docker ps
docker port co2meter-postgres
```

If you want to connect manually to the running container for further inspection

```shell
docker exec -it co2meter-postgres /bin/bash
psql -U postgres -d co2meter
```

Some Postgresql commands to inspect the DB.

```psql
\l - Display database
\c - Connect to database
\dn - List schemas
\dt - List tables inside public schemas
\dt schema1. - List tables inside particular schemas. For eg: 'schema1'.
```