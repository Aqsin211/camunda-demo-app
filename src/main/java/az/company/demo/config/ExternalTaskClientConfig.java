package az.company.demo.config;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalTaskClientConfig {

    @Value("${camunda.bpm.client.base-url}")
    private String baseUrl;

    @Value("${camunda.bpm.client.worker-id}")
    private String workerId;

    @Value("${camunda.bpm.client.lock-duration}")
    private long lockDuration;

    @Bean
    public ExternalTaskClient externalTaskClient() {
        return ExternalTaskClient.create()
                .baseUrl(baseUrl)
                .workerId(workerId)
                .lockDuration(lockDuration)
                .asyncResponseTimeout(10000)
                .build();
    }
}