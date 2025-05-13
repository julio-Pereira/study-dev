package com.bluebank.ingestor.service;

import com.bluebank.common.dto.TransactionRequest;
import com.bluebank.common.event.TransactionCreatedEvent;
import com.bluebank.common.model.TransactionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionPublisherService {

    private final PubSubService pubSubService;
    private final ObjectMapper objectMapper;
    private final RetryTemplate retryTemplate;
    private final Counter publishSuccessCounter;
    private final Counter publishFailureCounter;

    @Value("${pubsub.transaction.topic}")
    private String transactionTopic;

    public TransactionPublisherService(
            PubSubService pubSubService,
            ObjectMapper objectMapper,
            RetryTemplate retryTemplate,
            MeterRegistry meterRegistry) {
        this.pubSubService = pubSubService;
        this.objectMapper = objectMapper;
        this.retryTemplate = retryTemplate;
        this.publishSuccessCounter = Counter.builder("transaction.publish.success")
                .description("Number of transaction messages successfully published")
                .register(meterRegistry);

        this.publishFailureCounter = Counter.builder("transaction.publish.failure")
                .description("Number of transaction messages failed to publish")
                .register(meterRegistry);
    }

    /**
     * Publishes a transaction to PubSub with retry capability.
     *
     * @param request the transaction request to publish
     * @return the message ID from PubSub
     * @throws RuntimeException if publication fails after retries
     */
    public String publishTransaction(TransactionRequest request) {
        try {
            String transactionId = UUID.randomUUID().toString();
            TransactionCreatedEvent event = createEvent(request, transactionId);

            String eventJson = objectMapper.writeValueAsString(event);

            String messageId = retryTemplate.execute(retryContext -> {
               try {
                   return pubSubService.publish(transactionTopic, eventJson);
               } catch (ExecutionException e) {
                   log.warn("Failed to publish transaction, retrying... (Attempt: {})",
                           retryContext.getRetryCount() + 1,
                           e);
                   throw new RuntimeException(e);
               }
            });

            log.info(
                    "Published transaction with ID: {} to topic: {} with message ID: {}",
                    transactionId,
                    transactionTopic,
                    messageId
            );
            publishSuccessCounter.increment();
            return messageId;
        } catch (JsonProcessingException e) {
            publishFailureCounter.increment();
            log.error("Failed to serialize transaction event", e);
            throw new RuntimeException("Failed to serialize transaction event", e);
        } catch (Exception e) {
            publishFailureCounter.increment();
            log.error("Failed to publish transaction after retries", e);
            throw new RuntimeException("Failed to publish transaction after multiple attempts", e);
        }
    }

    private TransactionCreatedEvent createEvent(TransactionRequest request, String transactionId) {

        LocalDateTime timestamp = request.getTimestamp() != null ?
                request.getTimestamp() : LocalDateTime.now();

        return TransactionCreatedEvent.builder()
                .id(transactionId)
                .accountId(request.getAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .type(request.getType())
                .merchantName(request.getMerchantName())
                .merchantCategory(request.getMerchantCategory())
                .timestamp(timestamp)
                .status(TransactionStatus.RECEIVED)
                .eventId(UUID.randomUUID().toString())
                .eventTimestamp(LocalDateTime.now())
                .build();
    }

}
