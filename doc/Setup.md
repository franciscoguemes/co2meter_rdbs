Setup
==========================================================================

# Prerequisites

- Java 21
- Gradle 8.6
- docker 25.0.4

# Postgres DB

```shell
docker run --rm --name co2meter-postgres \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=co2meter \
  -d -p 5432:5432 postgres:alpine
docker ps
docker port co2meter-postgres
```

```shell
docker exec -it co2meter-postgres /bin/bash
psql -U postgres -d co2meter
```

```psql
\l - Display database
\c - Connect to database
\dn - List schemas
\dt - List tables inside public schemas
\dt schema1. - List tables inside particular schemas. For eg: 'schema1'.
```