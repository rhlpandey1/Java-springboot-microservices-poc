package org.example;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

// Wires up retry + dead-letter behaviour for ALL @KafkaListeners.
@Configuration
public class KafkaErrorConfig {

    // If a listener keeps throwing: retry twice (0.5s apart), then publish the
    // record to "<topic>.DLT". Spring Boot auto-uses this error handler.
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(500L, 2L));
    }

    // The dead-letter topic itself.
    @Bean
    public NewTopic dltTopic() {
        return TopicBuilder.name("order-confirmed.DLT").partitions(1).replicas(1).build();
    }
}
