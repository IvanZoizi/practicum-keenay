package org.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    @Bean
    public NewTopic newTopicEmailService() {
        return TopicBuilder.name("email-service").build();
    }

    @Bean
    public NewTopic newTopicEmailServiceRetry() {
        return TopicBuilder.name("email-retry").build();
    }

    @Bean
    public NewTopic newTopicNoRetryException() {
        return TopicBuilder.name("email-exception").build();
    }
}
