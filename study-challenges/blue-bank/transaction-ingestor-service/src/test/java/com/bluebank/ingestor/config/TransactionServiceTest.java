package com.bluebank.ingestor.config;

import com.bluebank.common.dto.TransactionRequest;
import com.bluebank.common.dto.TransactionResponse;
import com.bluebank.common.exception.ValidationException;
import com.bluebank.common.model.Currency;
import com.bluebank.common.model.TransactionStatus;
import com.bluebank.common.model.TransactionType;
import com.bluebank.ingestor.entity.TransactionAudit;
import com.bluebank.ingestor.repository.TransactionAuditRepository;
import com.bluebank.ingestor.service.RateLimitService;
import com.bluebank.ingestor.service.TransactionPublisherService;
import com.bluebank.ingestor.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionPublisherService publisherService;

    @Mock
    private TransactionAuditRepository auditRepository;

    @Mock
    private TransactionJdbcRepository transactionJdbcRepository;

    @Mock
    private RateLimitService rateLimitService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testProcessValidTransaction() throws Exception {
        // Given
        TransactionRequest request = new TransactionRequest().builder()
                .accountId("test-account")
                .amount(new BigDecimal("100.50"))
                .currency(Currency.BRL)
                .type(TransactionType.CREDIT_CARD)
                .merchantName("Test Merchant")
                .merchantCategory("Retail")
                .build();

        when(rateLimitService.checkAndUpdateRateLimit(anyString())).thenReturn(true);
        when(publisherService.publishTransaction(any(TransactionRequest.class))).thenReturn("test-message-id");
        when(auditRepository.save(any(TransactionAudit.class))).thenAnswer(i -> i.getArguments(0));

        // When
        TransactionResponse response = transactionService.processTransaction(request);


        // then
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(TransactionStatus.RECEIVED, response.getStatus());
        assertEquals("Transaction received successfully", response.getMessage());

        verify(rateLimitService).checkAndUpdateRateLimit("test-account");
        verify(publisherService).publishTransaction(request);
        verify(auditRepository).save(any(TransactionAudit.class));
    }

    @Test
    public void testProcessTransactionWithRateLimitExceeded() {
        // Given
        TransactionRequest request = new TransactionRequest().builder()
                .accountId("test-account")
                .amount(new BigDecimal("100.50"))
                .currency(Currency.BRL)
                .type(TransactionType.CREDIT_CARD)
                .merchantName("Test Merchant")
                .merchantCategory("Retail")
                .build();

        // When
        when(rateLimitService.checkAndUpdateRateLimit(anyString())).thenReturn(false);

        // Then
        assertThrows(ValidationException.class, () -> {
            transactionService.processTransaction(request);
        });

        verify(rateLimitService).checkAndUpdateRateLimit("test-account");
        verify(publisherService, never()).publishTransaction(any(TransactionRequest.class));
        verify(auditRepository, never()).save(any(TransactionAudit.class));
    }

    @Test
    public void testProcessTransactionWithPublishError() throws Exception {
        // Arrange
        TransactionRequest request = TransactionRequest.builder()
                .accountId("test-account")
                .amount(new BigDecimal("100.50"))
                .currency(Currency.BRL)
                .type(TransactionType.CREDIT_CARD)
                .merchantName("Test Merchant")
                .merchantCategory("Retail")
                .build();

        when(rateLimitService.checkAndUpdateRateLimit(anyString())).thenReturn(true);
        when(publisherService.publishTransaction(any(TransactionRequest.class)))
                .thenThrow(new RuntimeException("Failed to publish"));
        when(auditRepository.save(any(TransactionAudit.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        TransactionResponse response = transactionService.processTransaction(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(TransactionStatus.FAILED, response.getStatus());
        assertTrue(response.getMessage().contains("Failed to process transaction"));

        verify(rateLimitService).checkAndUpdateRateLimit("test-account");
        verify(publisherService).publishTransaction(request);
        verify(auditRepository).save(any(TransactionAudit.class));
    }


}
