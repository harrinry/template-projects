# Spring Template Projects - Simple Kafka Producer

This project is part of a set of sample projects that help illustrate how to use the Spring (Boot)
framework to accomplish the most common programming paradigms. The follow is a quick guide for how to use.

## What this Project Illustrates:

1. Send Kafka messages (a.k.a. "Kafka Producer")
   - sending metadata using Kafka headers
   - representing the `key` and `message` parts of the Kafka message as JSON
   - using a registered Future as a Kafka callback
2. Spring Boot Console Application
3. Layered Design
4. CSV file parsing
5. Strongly types application.properties file
6. Logging - via logback
7. Maven `pom.xml`

### Prerequisites

This project requires the following two prerequisites:
1. A running Kafka broker (see property `spring.kafka.consumer.bootstrap-servers` in `application-xxx.properties`)
2. A sample CSV file containing some sample Orders (see `sample-orders.csv` in `/main/resources`) 

### Environment Variables

You can set the following environment variables to control the configuration of the project.

- `KAFKA_BROKER_1`: address of Kafka broker #1, defaults to `localhost`
- `KAFKA_BROKER_1_PORT`: port of Kafka broker #1, defaults to port `9092` 

### Run project using Maven

```
mvn spring-boot:run
```

