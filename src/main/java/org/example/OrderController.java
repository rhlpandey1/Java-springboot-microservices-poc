package org.example;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

// PRODUCER: POST /orders publishes ONE "order-confirmed" event, then returns.
// orderId is optional so you can resend the SAME id to test idempotency,
// and item=poison lets you trigger the retry -> DLQ path.
@RestController
public class OrderController {

    private final KafkaTemplate<String, String> kafka;

    public OrderController(KafkaTemplate<String, String> kafka) {
        this.kafka = kafka;
    }

    @PostMapping("/orders")
    public Map<String, String> placeOrder(
            @RequestParam(defaultValue = "milk") String item,
            @RequestParam(required = false) String orderId) {

        String id = (orderId != null) ? orderId
                : UUID.randomUUID().toString().substring(0, 8);   // correlation id
        String event = "{\"orderId\":\"" + id + "\",\"item\":\"" + item + "\"}";

        kafka.send("order-confirmed", id, event);   // key=orderId -> same partition

        return Map.of("status", "accepted", "orderId", id);
    }
}
