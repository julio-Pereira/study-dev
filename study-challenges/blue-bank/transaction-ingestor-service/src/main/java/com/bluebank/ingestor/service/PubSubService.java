package com.bluebank.ingestor.service;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public String publish(String topic, String message) throws ExecutionException {
        ByteString data = ByteString.copyFrom(message, StandardCharsets.UTF_8);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .build();

        try {
            log.debug("Published message to topic {} - messageId: {}", topic, message);
            return publisher.publish(pubsubMessage).get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Timeout while publishing message to topic {}", topic, e);
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
