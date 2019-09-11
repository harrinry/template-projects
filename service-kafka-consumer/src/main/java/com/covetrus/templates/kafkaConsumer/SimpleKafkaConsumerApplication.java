package com.covetrus.templates.kafkaConsumer;

import com.covetrus.templates.kafkaConsumer.config.ApplicationProperties;
import com.covetrus.templates.kafkaConsumer.config.DefaultProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@SpringBootApplication(scanBasePackages = {"com.covetrus"})
@EnableConfigurationProperties({ApplicationProperties.class})
@Slf4j
public class SimpleKafkaConsumerApplication {
    @Autowired private Environment env;

    public static void main (String[] args) {
        SpringApplication app = new SpringApplication(SimpleKafkaConsumerApplication.class);

        // Add a default Spring profile in case one was not set at runtime.
        DefaultProfileUtil.addDefaultProfile(app);

        app.run(args);
    }

    @PostConstruct
    public void echoStartup() throws UnknownHostException {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }

        log.info("----------------------------------------------------------");
        log.info("Application ' {}' is running!", env.getProperty("spring.application.name"));
        log.info("  Local:      {}://localhost:{}", protocol, env.getProperty("server.port"));
        log.info("  External:   {}://{}:{}", protocol, InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"));
        log.info("  Kafka:      brokers={}, group-id={}, listenerEnabled={}, topicName={}", env.getProperty("spring.kafka.consumer.bootstrap-servers"), env.getProperty("spring.kafka.consumer.group-id"), env.getProperty("application.kafkaConsumer.listenerEnabled"), env.getProperty("application.kafkaConsumer.topicName"));
        log.info("  Profile(s): {}", Arrays.toString(env.getActiveProfiles()));
        log.info("----------------------------------------------------------");
    }

}
