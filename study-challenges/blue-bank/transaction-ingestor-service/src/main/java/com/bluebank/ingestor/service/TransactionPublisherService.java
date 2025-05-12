package com.bluebank.ingestor.service;

import com.bluebank.common.dto.TransactionRequest;
import com.bluebank.common.event.TransactionCreatedEvent;
import com.bluebank.common.model.TransactionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${pubsub.transaction.topic}")
    private String transactionTopic;

    public String publishTransaction(TransactionRequest request) {
        try {
            String transactionId = UUID.randomUUID().toString();

            TransactionCreatedEvent event = createEvent(request, transactionId);
            String eventJson = objectMapper.writeValueAsString(event);

            String messageId = pubSubService.publish(transactionTopic, eventJson);
            log.info(
                    "Published transaction with ID: {} to topic: {} with message ID: {}",
                    transactionId,
                    transactionTopic,
                    messageId
            );

            return messageId;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private TransactionCreatedEvent createEvent(TransactionRequest request, String transactionId) {

        LocalDateTime timestamp = request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now();

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
