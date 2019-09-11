# Spring Template Projects - Simple Kafka Consumer

This project is part of a set of sample projects that help illustrate how to use the Spring (Boot)
framework to accomplish the most common programming paradigms. The following is a quick guide for how to use.

## What this Project Illustrates:

1. Consume Kafka messages using the Spring Kafka `@KafkaListener` interface (a.k.a. "Kafka Consumer")
   - parse metadata fields from Kafka headers and client SDK
   - parse the `key` and `message` parts of the Kafka message as JSON
2. Spring Boot microservice (although with no initial REST APIs)
3. Implements `actuator` to implement health check and other default metrics (see https://www.baeldung.com/spring-boot-actuators)
4. Usage of `lombok` library to facilitate esy logging and cleaner POJOs (see https://projectlombok.org/).
5. Layered Design
6. Strongly typed application.properties file
7. Logging - via logback
8. Maven `pom.xml`

### Prerequisites

This project requires the following two prerequisites:
1. A running Kafka broker (see property `spring.kafka.consumer.bootstrap-servers` in `application-xxx.properties`)
2. A topic named `order` has been created in the broker.

### Environment Variables

You can set the following environment variables to control the configuration of the project.

- `PORT`: port that the consumer listens to (HTTP requests), defaults to `8080`
- `KAFKA_BROKER_1`: address of Kafka broker #1, defaults to `localhost`
- `KAFKA_BROKER_1_PORT`: port of Kafka broker #1, defaults to port `9092` 

### Run project using Maven

```
mvn spring-boot:run
```

