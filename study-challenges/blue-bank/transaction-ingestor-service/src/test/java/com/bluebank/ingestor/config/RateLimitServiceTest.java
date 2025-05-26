package com.bluebank.ingestor.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RateLimiteServiceTest {

    @Mock
    private RateLimitRepository rateLimitRepository;

    @InjectMocks
    private RateLimitService rateLimitService;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rateLimitService, "transactionsPerMinute", 10);
        ReflectionTestUtils.setField(rateLimitService, "windowSeconds", 60);
    }

    @Test
    void testCheckRateLimitForNewAccount() {
        // Given
        String accountId = "test-account";

        // When
        when(rateLimitRepository.findByKey(anyString())).thenReturn(Optional.empty());
        when(rateLimitRepository.save(any(RateLimit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> rateLimitService.checkRateLimit(accountId));

        // Then
        verify(rateLimitRepository).findByKey("transaction_" + accountId);
        verify(rateLimitRepository).save(any(RateLimit.class));
    }

    @Test
    void testCheckRateLimitForExistingAccountBelowLimit() {
        // Given
        String accountId = "test-account";
        RateLimit rateLimit = RateLimit.builder()
                .key("transaction_" + accountId)
                .count(5) // Below limit of 10
                .lastReset(LocalDateTime.now().minusSeconds(30))
                .lastUpdate(LocalDateTime.now().minusSeconds(10))
                .build();

        when(rateLimitRepository.findByKey(anyString())).thenReturn(Optional.of(rateLimit));
        when(rateLimitRepository.save(any(RateLimit.class))).thenAnswer(i -> i.getArgument(0));

        // When
        assertDoesNotThrow(() -> rateLimitService.checkRateLimit(accountId));

        // Then
        verify(rateLimitRepository).findByKey("transaction_" + accountId);
        verify(rateLimitRepository).save(any(RateLimit.class));
        assertEquals(6, rateLimit.getCount()); // Count should be incremented
    }

    @Test
    void testCheckRateLimitForExistingAccountAtLimit() {
        // Given
        String accountId = "test-account";
        RateLimit rateLimit = RateLimit.builder()
                .key("transaction_" + accountId)
                .count(10) // At limit
                .lastReset(LocalDateTime.now().minusSeconds(30))
                .lastUpdate(LocalDateTime.now().minusSeconds(10))
                .build();

        when(rateLimitRepository.findByKey(anyString())).thenReturn(Optional.of(rateLimit));

        // When & Then
        RateLimitExceededException exception = assertThrows(RateLimitExceededException.class,
                () -> rateLimitService.checkRateLimit(accountId));

        assertTrue(exception.getMessage().contains("Rate limit of 10 requests"));
        verify(rateLimitRepository).findByKey("transaction_" + accountId);
        verify(rateLimitRepository, never()).save(any(RateLimit.class));
    }

    @Test
    void testCheckRateLimitForExistingAccountNeedingReset() {
        // Given
        String accountId = "test-account";
        RateLimit rateLimit = RateLimit.builder()
                .key("transaction_" + accountId)
                .count(15) // Above limit but old
                .lastReset(LocalDateTime.now().minusSeconds(70)) // Over window of 60 seconds
                .lastUpdate(LocalDateTime.now().minusSeconds(5))
                .build();

        when(rateLimitRepository.findByKey(anyString())).thenReturn(Optional.of(rateLimit));
        when(rateLimitRepository.save(any(RateLimit.class))).thenAnswer(i -> i.getArgument(0));

        // When
        assertDoesNotThrow(() -> rateLimitService.checkRateLimit(accountId));

        // Then
        verify(rateLimitRepository).findByKey("transaction_" + accountId);
        verify(rateLimitRepository).save(any(RateLimit.class));
        assertEquals(1, rateLimit.getCount()); // Count should be reset to 1
    }

    @Test
    void testCheckAndUpdateRateLimitForLegacyMethod() {
        // Given
        String accountId = "test-account";
        RateLimit rateLimit = RateLimit.builder()
                .key("transaction_" + accountId)
                .count(10) // At limit
                .lastReset(LocalDateTime.now().minusSeconds(30))
                .lastUpdate(LocalDateTime.now().minusSeconds(10))
                .build();

        when(rateLimitRepository.findByKey(anyString())).thenReturn(Optional.of(rateLimit));

        // When
        boolean result = rateLimitService.checkAndUpdateRateLimit(accountId);

        // Then
        assertFalse(result);
        verify(rateLimitRepository).findByKey("transaction_" + accountId);
        verify(rateLimitRepository, never()).save(any(RateLimit.class));
    }
}
