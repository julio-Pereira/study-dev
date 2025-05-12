package com.bluebank.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FinancialTransactionTest {

    @Test
    public void testCreateNew() {
        // Given
        String accountId = "account123";
        BigDecimal amount = new BigDecimal("100.50");
        Currency currency = Currency.BRL;
        TransactionType type = TransactionType.CREDIT_CARD;

        // When
        FinancialTransaction transaction = FinancialTransaction.createTransaction(
                accountId, amount, currency, type);

        // Then
        assertNotNull(transaction);
        assertNotNull(transaction.getId());
        assertEquals(accountId, transaction.getAccountId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(currency, transaction.getCurrency());
        assertEquals(type, transaction.getType());
        assertEquals(TransactionStatus.RECEIVED, transaction.getStatus());
        assertNotNull(transaction.getTimestamp());
        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getUpdatedAt());
        assertEquals(FraudRiskLevel.LOW, transaction.getFraudRiskLevel());
    }

    @Test
    public void testIsAmountPositive() {
        // Given
        FinancialTransaction positiveTransaction = FinancialTransaction.builder()
                .amount(new BigDecimal("100.50"))
                .build();

        FinancialTransaction zeroTransaction = FinancialTransaction.builder()
                .amount(BigDecimal.ZERO)
                .build();

        FinancialTransaction negativeTransaction = FinancialTransaction.builder()
                .amount(new BigDecimal("-50.25"))
                .build();

        // When & Then
        assertTrue(positiveTransaction.isAmountPositive());
        assertFalse(zeroTransaction.isAmountPositive());
        assertFalse(negativeTransaction.isAmountPositive());
    }

    @Test
    public void testUpdateStatus() {
        // Given
        FinancialTransaction transaction = FinancialTransaction.builder()
                .status(TransactionStatus.RECEIVED)
                .updatedAt(LocalDateTime.now().minusHours(1))
                .build();

        var initialUpdatedAt = transaction.getUpdatedAt();

        // When
        transaction.updateStatus(TransactionStatus.PROCESSING);

        // Then
        assertEquals(TransactionStatus.PROCESSING, transaction.getStatus());
        assertTrue(transaction.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    public void testSetFraudRiskFromScore() {
        // Arrange
        FinancialTransaction transaction = FinancialTransaction.builder()
                .status(TransactionStatus.PROCESSING)
                .build();

        // Act
        transaction.setFraudRiskFromScore(0.2); // LOW

        // Assert
        assertEquals(FraudRiskLevel.LOW, transaction.getFraudRiskLevel());
        assertEquals(TransactionStatus.PROCESSING, transaction.getStatus()); // Status shouldn't change

        // Act again with high risk
        transaction.setFraudRiskFromScore(0.8); // HIGH

        // Assert
        assertEquals(FraudRiskLevel.HIGH, transaction.getFraudRiskLevel());
        assertEquals(TransactionStatus.FLAGGED_FOR_REVIEW, transaction.getStatus()); // Status should change
    }

    @Test
    public void testSetFraudRiskFromScoreWithInvalidScore() {
        // Arrange
        FinancialTransaction transaction = FinancialTransaction.builder().build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transaction.setFraudRiskFromScore(-0.1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            transaction.setFraudRiskFromScore(1.1);
        });
    }
}
