package org.example;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// CONSUMER #3 (delivery-service): another independent group -> also gets every event.
@Component
public class DeliveryConsumer {

    @KafkaListener(topics = "order-confirmed", groupId = "delivery-service")
    public void onOrder(ConsumerRecord<String, String> rec) {
        System.out.println("[delivery] assigning rider  orderId=" + rec.key());
    }
}
