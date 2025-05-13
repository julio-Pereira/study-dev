package com.bluebank.ingestor.config;

import com.bluebank.common.dto.TransactionRequest;
import com.bluebank.common.event.TransactionCreatedEvent;
import com.bluebank.common.model.Currency;
import com.bluebank.common.model.TransactionType;
import com.bluebank.ingestor.service.PubSubService;
import com.bluebank.ingestor.service.TransactionPublisherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionPublisherServiceTest {

    @Mock
    private PubSubService pubSubService;

    private ObjectMapper objectMapper;
    private RetryTemplate retryTemplate;
    private MeterRegistry meterRegistry;
    private TransactionPublisherService publisherService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        retryTemplate = new RetryTemplate();
        meterRegistry = new SimpleMeterRegistry();

        publisherService = new TransactionPublisherService(
                pubSubService,
                objectMapper,
                retryTemplate,
                meterRegistry
        );

        ReflectionTestUtils.setField(publisherService, "transactionTopic", "transactions");
    }

    @Test
    void publishTransactionSuccessful() throws Exception {
        // Arrange
        TransactionRequest request = createSampleTransactionRequest();
        when(pubSubService.publish(eq("transactions"), anyString())).thenReturn("test-message-id");

        // Act
        String messageId = publisherService.publishTransaction(request);

        // Assert
        assertEquals("test-message-id", messageId);
        assertEquals(1, meterRegistry.get("transaction.publish.success").counter().count());
        assertEquals(0, meterRegistry.get("transaction.publish.failure").counter().count());

        // Verify the correct JSON was sent to PubSub
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(pubSubService).publish(eq("transactions"), jsonCaptor.capture());

        String capturedJson = jsonCaptor.getValue();
        TransactionCreatedEvent event = objectMapper.readValue(capturedJson, TransactionCreatedEvent.class);
        assertEquals(request.getAccountId(), event.getAccountId());
        assertEquals(request.getAmount(), event.getAmount());
        assertEquals(request.getCurrency(), event.getCurrency());
        assertEquals(request.getType(), event.getType());
        assertNotNull(event.getId()); // Transaction ID should be generated
    }

    @Test
    void publishTransactionFailure() throws Exception {
        // Arrange
        TransactionRequest request = createSampleTransactionRequest();
        when(pubSubService.publish(eq("transactions"), anyString()))
                .thenThrow(new ExecutionException("Test execution error", new RuntimeException()));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            publisherService.publishTransaction(request);
        });

        assertEquals(0, meterRegistry.get("transaction.publish.success").counter().count());
        assertEquals(1, meterRegistry.get("transaction.publish.failure").counter().count());
    }

    @Test
    void publishTransactionJsonError() throws Exception {
        // Arrange
        TransactionRequest request = createSampleTransactionRequest();

        // Create a spy of objectMapper that will throw when writing as string
        ObjectMapper spyMapper = spy(objectMapper);
        doThrow(new com.fasterxml.jackson.core.JsonProcessingException("Test JSON error") {})
                .when(spyMapper).writeValueAsString(any());

        // Replace the objectMapper in the service
        publisherService = new TransactionPublisherService(
                pubSubService, spyMapper, retryTemplate, meterRegistry);
        ReflectionTestUtils.setField(publisherService, "transactionTopic", "transactions");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            publisherService.publishTransaction(request);
        });

        assertEquals(0, meterRegistry.get("transaction.publish.success").counter().count());
        assertEquals(1, meterRegistry.get("transaction.publish.failure").counter().count());
        verify(pubSubService, never()).publish(anyString(), anyString());
    }

    private TransactionRequest createSampleTransactionRequest() {
        return TransactionRequest.builder()
                .accountId("test-account")
                .amount(new BigDecimal("100.50"))
                .currency(Currency.BRL)
                .type(TransactionType.CREDIT_CARD)
                .merchantName("Test Merchant")
                .merchantCategory("Retail")
                .build();
    }



}
