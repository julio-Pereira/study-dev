package com.bluebank.ingestor.config;

import com.bluebank.ingestor.service.PubSubService;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PubSubServiceTest {

    @Mock
    private Publisher publisher;

    @Mock
    private ApiFuture<String> apiFuture;

    private MeterRegistry meterRegistry;
    private PubSubService pubSubService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        pubSubService = new PubSubService(publisher, meterRegistry);
    }


    @Test
    void publishSuccessful() throws Exception {
        // Arrange
        String expectedMessageId = "test-message-id";
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);
        when(apiFuture.get(anyLong(), any(TimeUnit.class))).thenReturn(expectedMessageId);

        // Act
        String actualMessageId = pubSubService.publish("test-topic", "test-message");

        // Assert
        assertEquals(expectedMessageId, actualMessageId);
        assertEquals(1, meterRegistry.get("pubsub.publish.success").counter().count());
        assertEquals(0, meterRegistry.get("pubsub.publish.failure").counter().count());
        verify(publisher, times(1)).publish(any(PubsubMessage.class));
    }

    @Test
    void publishTimeout() throws Exception {
        // Arrange
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);
        when(apiFuture.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("Test timeout"));

        // Act & Assert
        assertThrows(ExecutionException.class, () -> {
            pubSubService.publish("test-topic", "test-message");
        });

        assertEquals(0, meterRegistry.get("pubsub.publish.success").counter().count());
        assertEquals(1, meterRegistry.get("pubsub.publish.failure").counter().count());
        verify(publisher, times(1)).publish(any(PubsubMessage.class));
    }

    @Test
    void publishExecutionError() throws Exception {
        // Arrange
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);
        when(apiFuture.get(anyLong(), any(TimeUnit.class))).thenThrow(
                new ExecutionException("Test execution error", new RuntimeException()));

        // Act & Assert
        assertThrows(ExecutionException.class, () -> {
            pubSubService.publish("test-topic", "test-message");
        });

        assertEquals(0, meterRegistry.get("pubsub.publish.success").counter().count());
        assertEquals(1, meterRegistry.get("pubsub.publish.failure").counter().count());
        verify(publisher, times(1)).publish(any(PubsubMessage.class));
    }

}
