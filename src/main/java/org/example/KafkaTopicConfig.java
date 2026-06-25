package org.example;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// Auto-creates the topic on startup (1 partition, 1 replica — fine for local).
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderConfirmed() {
        return TopicBuilder.name("order-confirmed").partitions(1).replicas(1).build();
    }
}
