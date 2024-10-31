# Spring Boot 3 App

## Running application 
- docker compose -f deployment/compose/infra.yml -d
- docker compose -f deployment/compose/monitor.yml -d
- docker compose -f deployment/compose/elk.yml -d
- mnv spring-boot:run

## Running with [Taskfile](https://taskfile.dev/)
- task start-infra
- task start-monitoring
- task start-elk
- task start-recipe-app
- 
## Running with [Taskfile](https://taskfile.dev/): single command
- task start-recipe-all

### Technologies Stack (Development)
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway Migration
- JWT
- Docker Compose
- Taskfile

### Monitoring Stack
- Micrometer Prometheus
- Grafana

### Logging and Tracing Stack
- Elasticsearch 
- Kibana
- Logstash
- Micrometer Bridge Otel


### Add User to Elasticsearch for Kibana step-by-step
- docker exec -it elasticsearch-rs bash ./bin/elasticsearch-users useradd kibana-user -p password -r kibana_system

or you can use [Taskfile](https://taskfile.dev/)
- task start-elk

### Endpoints for tools Passwords

- Postgresql psql & pgdmin
  - url: http://localhost:15432
  - username: recipe_user
  - password: recipe_secret
  - command: psql -U recipe_user --port 15432 recipe_db


- [Prometheus UI via http://localhost:9090](http://localhost:9090)


- [Kibana UI via http://localhost:5601](http://localhost:5601)
  - username: elastic
  - password: password


- [Grafana UI http://localhost:3000](http://localhost:3000)
  - username: admin
  - password: admin


## Will be added
- DTOs & request params validations
- Roles for Users
- Dockerize the app