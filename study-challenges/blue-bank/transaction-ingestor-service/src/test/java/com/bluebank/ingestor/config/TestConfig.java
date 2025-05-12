package com.bluebank.ingestor.config;

import com.bluebank.ingestor.service.PubSubService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {

    /**
     * Creates a mock PubSubService for testing.
     *
     * @return the mock PubSubService
     */
    @Bean
    @Primary
    public PubSubService mockPubSubService() throws Exception {
        PubSubService mockService = mock(PubSubService.class);
        when(mockService.publish(anyString(), anyString())).thenReturn("test-message-id");
        return mockService;
    }
}
