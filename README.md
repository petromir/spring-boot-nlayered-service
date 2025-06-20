# Spring Boot project using N-layered architecture

A production-ready example of Spring Boot project using N-layered architecture. 

Layers are virtual separations within an application that organize its different parts, such as the presentation layer (UI), the business logic layer, and the data access layer.

## Status
**The project is still in an early phase!**

## Environment

Java and Maven are managed by SDKMAN. To install the defined versions in `.sdkmanrc` file run the command:
```shell
sdk env install
```

## Database

### Instance
- Local Development & Testing - Docker Compose manages the database
- CI/CD - the database is managed by the CI/CD system

### Migrations

Database changes are tracked and executed using a third-party tool - [Flyway](https://flywaydb.org/) or
[Liquibase](https://www.liquibase.org/). Each of these tools is able to manage database migrations, so you
don't have to implement an in-house solution.

#### Flows

##### Local development

Spring Boot native integrations of Docker Compose and Liquibase are responsible for initializing the DB (defined as
part of the docker-compose.yaml file (add a link to the file) and run the specified migrations. The DB will not be 
stopped as part of the application shutdown to allow faster development cycles.

##### Local testing

Same as development, but DB is stopped, so we have fresh DB on every test run.

##### CI/CD PR evaluation

It's assumed that DB is already up&running, so Docker-compose is disabled. Spring Boot Liquibase native integration
runs the specified migrations.

##### CI/CD deployment to environments (dev/stage/prod)
The general concept is to execute the migrations before the application initialization when deploying the service in 
the cloud. A Docker image is created as part of each build when code is merged to the corresponding branch by copying 
the specified migrations in the image. Then the image is executed against a specific database server and migrations 
are applied. Only when the database migration is successful, then we move to application deployment.

Running database migrations on an application startup is not recommended because:
- The startup is slower to the point where schedulers like Kubernetes may kill them (pay attention to liveness vs.
  readiness probes)
- The application must run on a more privileged DB user supporting schema updates
- Other instances may be blocked until the first one completes migrations
- If you don't have redundancy, you get downtime during migration

That being said, consider running migration automatically prior to deployment.

#### Definition

Database migrations are defined in their default place depending on the selected tool (add links to the folders)

- Local build

```shell
docker build -t liquibase-migrations:latest -f docker/db-migrations/Dockerfile.liquibase .
```

- CI/CD Build

```shell
docker build -t liquibase-migrations:<desired_tag> -f docker/db-migrations/Dockerfile.liquibase .
```

#### Execution

- Local

```shell
 docker run --network=database -e DB_USERNAME=postgres -e DB_PASSWORD=password -e DB_URL=jdbc:postgresql://db:5432/spring_boot_nlayered_service liquibase-migrations:latest
```

- CI/CD

```shell
 docker run -e DB_USERNAME=<env_db_username> -e DB_PASSWORD=<env_db_password> -e 
 DB_URL=jdbc:postgresql://<env_db_host>:<env_db_port>/spring_boot_nlayered_service 
 liquibase-migrations:<docker_repo_tag>
```
by replacing the variables wrapped in `<>`

