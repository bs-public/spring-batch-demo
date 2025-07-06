# Spring Batch Demo

A demo Spring Boot application using Spring Batch to import large datasets into a MySQL database. Batch jobs are triggered via a REST endpoint instead of running automatically on startup. Spring Batch metadata tables are created automatically if needed.

---

### Features

- Imports large datasets into MySQL using efficient chunked JDBC batching.
- Spring Batch metadata tables are automatically created (if not present).
- Jobs are not auto-run on app startup. Trigger them via a REST endpoint.

---

### Prerequisites

- Java 24
- Maven
- Docker & Docker Compose

---

### Configuration

```properties
# Start Docker Compose services with the app, but don't stop them on exit
spring.docker.compose.lifecycle-management=start-only

# Always run schema/data SQL scripts for app tables
spring.sql.init.mode=always

# Auto-create Spring Batch metadata tables if needed
spring.batch.jdbc.initialize-schema=always

# Disable auto-running jobs on startup
spring.batch.job.enabled=false
```

### REST endpoint to start Job
```
curl -X POST http://localhost:8080/batch/import
```
