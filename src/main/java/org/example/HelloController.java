package org.example;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

// REST endpoints
@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello from a Dockerized Java (Spring Boot) microservice on Kubernetes";
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok!! I am cool");   // returned as JSON automatically
    }
}
