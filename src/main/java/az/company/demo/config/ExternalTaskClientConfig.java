package az.company.demo.config;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalTaskClientConfig {

    @Bean
    public ExternalTaskClient externalTaskClient() {
        return ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .workerId("order-service-worker")
                .lockDuration(10000)
                .asyncResponseTimeout(10000)
                .build();
    }
}