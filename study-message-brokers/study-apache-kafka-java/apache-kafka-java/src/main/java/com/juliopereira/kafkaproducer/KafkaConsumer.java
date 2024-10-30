package com.juliopereira.kafkaproducer;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "test-topic", groupId = "group-1")
    public void consumeMessage(String message) {
        log.info(String.format("#### -> Consumed message -> %s", message));
    }
}
