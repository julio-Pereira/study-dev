package com.bluebank.common.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialTransaction {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private Currency currency;
    private TransactionType type;
    private String merchantName;
    private String merchantCategory;
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FraudRiskLevel fraudRiskLevel;

    @Builder.Default
    private Map<String, Object> additionalData = new HashMap<>();


    /**
     * Factory method to create a new transaction with default values.
     *
     * @param accountId the account ID
     * @param amount the transaction amount
     * @param currency the transaction currency
     * @param type the transaction type
     * @return a new FinancialTransaction with default values
     */
    public static FinancialTransaction createTransaction(String accountId, BigDecimal amount, Currency currency, TransactionType type) {
        LocalDateTime now = LocalDateTime.now();
        return FinancialTransaction.builder()
                .id(UUID.randomUUID().toString())
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .type(type)
                .timestamp(now)
                .status(TransactionStatus.RECEIVED)
                .createdAt(now)
                .updatedAt(now)
                .fraudRiskLevel(FraudRiskLevel.LOW)
                .build();
    }

    /**
     * Checks if the transaction amount is positive.
     *
     * @return true if the amount is positive, false otherwise
     */
    public boolean isAmountPositive() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Updates the transaction status and updates the updated timestamp.
     *
     * @param newStatus the new status
     */
    public void updateStatus(TransactionStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the fraud risk level and updates the updated timestamp.
     *
     * @param riskScore the new fraud risk level
     */
    public void setFraudRiskFromScore(double riskScore) {
        this.fraudRiskLevel = FraudRiskLevel.fromScore(riskScore);

        if (this.fraudRiskLevel.isRequiresReview() && this.status != TransactionStatus.FLAGGED_FOR_REVIEW) {
            updateStatus(TransactionStatus.FLAGGED_FOR_REVIEW);
        }
    }

}
