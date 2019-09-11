package com.covetrus.templates.kafkaConsumer.service;

import com.covetrus.templates.kafkaConsumer.domain.KafkaMetadata;
import com.covetrus.templates.kafkaConsumer.domain.Order;
import com.covetrus.templates.kafkaConsumer.domain.event.Event;
import com.covetrus.templates.kafkaConsumer.exception.InvalidFieldsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {
    private static final double MAX_ORDER_AMOUNT = 1000;

    // Process an order
    public void processEvent(final KafkaMetadata kafkaMetadata, final Event event) {
        log.debug("Order processed: kafka=[topic={}, offset={}, partition={}, timestamp={}]" +
                                  " event.key=[delim={}, partNames={}, partValues={}]" +
                                  " event.object=[{}]",
                kafkaMetadata.getTopic(), kafkaMetadata.getOffset(), kafkaMetadata.getPartition(), kafkaMetadata.getTimestamp().toString(),
                event.getKey().getDelim(), event.getKey().getPartNames(), event.getKey().getPartValues(),
                event.getObject()
        );

        if (event.getObject() instanceof Order) {
            Order order = (Order) event.getObject();

            long primaryKey = Long.parseLong(event.getKey().getPartValues());
            log.debug("--> this Order has a primary key of {}", primaryKey);

            if (order.getAmount() > MAX_ORDER_AMOUNT) {
                InvalidFieldsException ex = new InvalidFieldsException();
                ex.addError("amount", "Order amount cannot be more than $1,000.");
                throw ex;
            }
        }
    }
}
