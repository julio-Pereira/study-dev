package com.bluebank.ingestor.entity;

import com.bluebank.common.model.Currency;
import com.bluebank.common.model.TransactionStatus;
import com.bluebank.common.model.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions_audit", schema = "transaction_ingestor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_category")
    private String merchantCategory;

    @Column(name = "transaction_timestamp", nullable = false)
    private LocalDateTime transactionTimestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "pubsub_message_id")
    private String pubsubMessageId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "error_message")
    private String errorMessage;


    /**
     * Factory method to create a successful audit entry.
     */
    public static TransactionAudit createSuccess(
            String transactionId,
            String accountId,
            BigDecimal amount,
            Currency currency,
            TransactionType type,
            String merchantName,
            String merchantCategory,
            LocalDateTime transactionTimestamp,
            String pubsubMessageId) {

        return TransactionAudit.builder()
                .transactionId(transactionId)
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .type(type)
                .merchantName(merchantName)
                .merchantCategory(merchantCategory)
                .transactionTimestamp(transactionTimestamp)
                .pubsubMessageId(pubsubMessageId)
                .status(TransactionStatus.RECEIVED)
                .build();
    }

    /**
     * Factory method to create a failed audit entry.
     */
    public static TransactionAudit createFailure(
            String transactionId,
            String accountId,
            BigDecimal amount,
            Currency currency,
            TransactionType type,
            String merchantName,
            String merchantCategory,
            LocalDateTime transactionTimestamp,
            String errorMessage) {

        return TransactionAudit.builder()
                .transactionId(transactionId)
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .type(type)
                .merchantName(merchantName)
                .merchantCategory(merchantCategory)
                .transactionTimestamp(transactionTimestamp)
                .status(TransactionStatus.FAILED)
                .errorMessage(errorMessage)
                .build();
    }
}
