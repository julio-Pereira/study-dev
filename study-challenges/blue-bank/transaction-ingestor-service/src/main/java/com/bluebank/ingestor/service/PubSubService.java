package com.bluebank.ingestor.service;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PubSubService {

    private final Publisher publisher;
    private final MeterRegistry meterRegistry;

    private final Counter publishSuccessCounter;
    private final Counter publishFailureCounter;
    private final Timer publishLatencyTimer;


    public PubSubService(Publisher publisher, MeterRegistry meterRegistry) {
        this.publisher = publisher;
        this.meterRegistry = meterRegistry;

        this.publishSuccessCounter = Counter.builder("pubsub.publish.success")
                .description("Number of messages successfully published")
                .register(meterRegistry);

        this.publishFailureCounter = Counter.builder("pubsub.publish.failure")
                .description("Number of messages failed to publish")
                .register(meterRegistry);

        this.publishLatencyTimer = Timer.builder("pubsub.publish.latency")
                .description("Time taken to publish messages")
                .register(meterRegistry);
    }

    /**
     * Publishes a message to the specified topic with retry capability.
     *
     * @param topic The topic to publish to
     * @param message The message to publish
     * @return The published message ID
     * @throws ExecutionException If the publishing fails after retries
     */
    @Retryable(
            value = {ExecutionException.class, TimeoutException.class, InterruptedException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2)
    )
    public String publish(String topic, String message) throws ExecutionException, InterruptedException {
        ByteString data = ByteString.copyFrom(message, StandardCharsets.UTF_8);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .build();

        try {
            Timer.Sample sample = Timer.start();
            String meessageId = publisher.publish(pubsubMessage).get(10, TimeUnit.SECONDS);
            sample.stop(publishLatencyTimer);

            log.debug("Published message to topic {} - messageId: {}", topic, message);
            publishSuccessCounter.increment();
            return meessageId;
        } catch (TimeoutException e) {
            log.error("Timeout while publishing message to topic {}", topic, e);
            publishSuccessCounter.increment();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            log.error("Interrupted while publishing message to topic {}", topic, e);
            publishFailureCounter.increment();
            Thread.currentThread().interrupt();
            throw e;
        } catch (ExecutionException e) {
            log.error("Error publishing message to topic {}", topic, e);
            publishFailureCounter.increment();
            throw e;
        }
    }

}
