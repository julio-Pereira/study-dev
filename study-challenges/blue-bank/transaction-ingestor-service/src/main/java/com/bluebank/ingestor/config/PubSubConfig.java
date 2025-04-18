package com.bluebank.ingestor.config;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class PubSubConfig {

    @Value("${pubsub.transaction.topic}")
    private String transactionTopic;

    @Value("${pubsub.transaction.projectId}")
    private String projectId;

    @Value("${pubsub.emulator.enabled:false}")
    private boolean emulatorEnabled;

    @Value("${pubsub.emulator.host:localhost:8085}")
    private String emulatorHost;

    /**
     * Creates a Publisher instance for the transaction topic.
     * If using the emulator, it sets the appropriate environment variable.
     */
    @Bean
    public Publisher transactionPublisher() throws IOException {
        if (emulatorEnabled) {
            log.info("Using PubSub emulator at host: {}", emulatorHost);
            System.setProperty("PUBSUB_EMULATOR_HOST", emulatorHost);
        } else {
            log.info("Using GCP PubSub");
            // Clear the property in case it was set elsewhere
            System.clearProperty("PUBSUB_EMULATOR_HOST");
        }

        TopicName topicName = TopicName.of(projectId, transactionTopic);
        log.info("Creating publisher for topic: {}", topicName);

        return Publisher.newBuilder(topicName).build();
    }
}
