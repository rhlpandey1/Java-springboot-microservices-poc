package org.example;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// CONSUMER #1 (inventory-service): idempotent + can fail to a DLQ.
@Component
public class InventoryConsumer {

    // remembers orderIds it already handled -> IDEMPOTENCY
    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = "order-confirmed", groupId = "inventory-service")
    public void onOrder(ConsumerRecord<String, String> rec) {
        String orderId = rec.key();

        // 1) Idempotency: at-least-once delivery can repeat a message.
        if (processed.contains(orderId)) {
            System.out.println("[inventory] DUPLICATE skipped  orderId=" + orderId);
            return;
        }

        // 2) Poison message demo: throwing triggers retries -> DLQ.
        if (rec.value().contains("poison")) {
            throw new RuntimeException("cannot process poison order " + orderId);
        }

        // 3) Success -> mark processed AFTER it worked.
        processed.add(orderId);
        System.out.println("[inventory] decremented stock  orderId=" + orderId
                + "  offset=" + rec.offset());
    }
}