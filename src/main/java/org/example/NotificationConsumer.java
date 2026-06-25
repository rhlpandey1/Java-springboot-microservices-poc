package org.example;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// CONSUMER #2 (notification-service): its OWN group, so it gets EVERY event
// independently of inventory-service. This is FAN-OUT.
@Component
public class NotificationConsumer {

    @KafkaListener(topics = "order-confirmed", groupId = "notification-service")
    public void onOrder(ConsumerRecord<String, String> rec) {
        System.out.println("[notification] sent push/SMS  orderId=" + rec.key());
    }
}
