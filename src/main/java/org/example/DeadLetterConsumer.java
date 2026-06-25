package org.example;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// Monitors the dead-letter topic. Poison messages that inventory-service
// could not process (after retries) land here for investigation.
@Component
public class DeadLetterConsumer {

    @KafkaListener(topics = "order-confirmed.DLT", groupId = "dlq-monitor")
    public void onDeadLetter(ConsumerRecord<String, String> rec) {
        System.out.println("[DLQ] poison parked  orderId=" + rec.key()
                + "  value=" + rec.value());
    }
}
