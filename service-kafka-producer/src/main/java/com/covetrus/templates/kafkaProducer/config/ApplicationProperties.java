package com.covetrus.templates.kafkaProducer.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
  All the application's configuration settings.  These are embedded within the application.properties files and are prefixed with 'application'.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Slf4j
public class ApplicationProperties {
    @Getter private final KafkaProducer kafkaProducer = new KafkaProducer();
    @Getter private final Logging logging = new Logging();

    public static class KafkaProducer {
        @Getter @Setter private String topicName = "<not-specified>";
        @Getter @Setter private boolean sendMessages = true;
        @Getter private final HeaderNames headerNames = new HeaderNames();
    }

    public static class HeaderNames {
        @Getter @Setter private String publisher = "<not-specified>";
        @Getter @Setter private String type = "<not-specified>";
        @Getter @Setter private String event = "<not-specified>";
        @Getter @Setter private String traceId = "<not-specified>";
        @Getter @Setter private String version = "<not-specified>";
    }

    public static class Logging {
        @Getter private final Logstash logstash = new Logstash();

        public static class Logstash {
            @Getter @Setter private boolean enabled = false;
            @Getter @Setter private String host = "localhost";
            @Getter @Setter private int port = 5000;
            @Getter @Setter private int queueSize = 512;
        }
    }
}
