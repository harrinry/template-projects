package com.covetrus.templates.kafkaProducer;

import com.covetrus.templates.kafkaProducer.config.ApplicationProperties;
import com.covetrus.templates.kafkaProducer.config.DefaultProfileUtil;
import com.covetrus.templates.kafkaProducer.domain.Order;
import com.covetrus.templates.kafkaProducer.domain.OrderStatusEnum;
import com.covetrus.templates.kafkaProducer.service.OrderService;
import com.covetrus.templates.kafkaProducer.util.FieldFormatHelper;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.covetrus"})
@EnableConfigurationProperties({ApplicationProperties.class})
@Slf4j
public class SimpleKafkaProducerApplication implements CommandLineRunner {
    @Autowired private Environment env;
    @Autowired private OrderService orderService;

    public static void main (String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(SimpleKafkaProducerApplication.class);

        // Add a default Spring profile in case one was not set at runtime.
        DefaultProfileUtil.addDefaultProfile(app);

        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        if (args.length != 1) {
            System.out.println("\nInvalid program arguments");
            System.out.println("  Usage: SimpleKafkaProducerApplication <full-filename.csv>\n");
            throw new ApplicationContextException("Invalid program arguments");
        }

        echoStartup(args);

        String csvFilename = args[0];

        List<Order> orderList = fetchOrders(csvFilename);

        orderService.processOrders(orderList);

        log.info("Producer job completed");
    }

    private List<Order> fetchOrders(final String csvFilename) throws IOException {
        List<Order> orderList = new ArrayList<>();

        CSVReader reader = null;

        File f = ResourceUtils.getFile(csvFilename);

        reader = new CSVReader(new FileReader(f.getAbsolutePath()));
        reader.skip(1); // skip header record

        String[] line;
        int i = 1;

        while ((line = reader.readNext()) != null) {
            Order order = new Order();
            order.setOrderId(Long.parseLong(line[0]));
            order.setGuid(line[1]);
            order.setAmount(Double.parseDouble(line[2]));
            order.setDateCreated(FieldFormatHelper.getZonedDateTimeFromString(line[3]));
            order.setCreatedBy(line[4]);
            order.setStatus(OrderStatusEnum.create(line[5]));
            orderList.add(order);
            log.debug("loaded order {}: [id={}, guid={}, amount={}]", i, order.getOrderId(), order.getGuid(), order.getAmount());
            i++;
        }

        return orderList;
    }

    private void echoStartup(String[] args) {
        log.info("----------------------------------------------------------");
        log.info("Application ' {}' is running!", env.getProperty("spring.application.name"));
        log.info("  Description: {}", env.getProperty("info.app.description"));
        log.info("  Orders file: {}", args[0]);
        log.info("  Kafka:      brokers={}, topicName={}, sendMessages={}", env.getProperty("spring.kafka.consumer.bootstrap-servers"), env.getProperty("application.kafkaProducer.topicName"), env.getProperty("application.kafkaProducer.sendMessages"));
        log.info("  Profile(s): {}", Arrays.toString(env.getActiveProfiles()));
        log.info("----------------------------------------------------------");
    }

}
