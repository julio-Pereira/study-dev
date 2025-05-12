package com.bluebank.common.event;

import com.bluebank.common.model.FraudRiskLevel;
import com.bluebank.common.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Event published when a transaction has been processed.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class TransactionProcessedEvent {
    private String transactionId;
    private TransactionStatus status;
    private FraudRiskLevel fraudRiskLevel;
    private LocalDateTime processedAt;
    private String eventId;
    private LocalDateTime eventTimestamp;

    @Builder.Default
    private Map<String, Object> enrichmentData = new HashMap<>();

    /**
     * Factory method to create a new event.
     *
     * @param transactionId the transaction ID
     * @param status the transaction status
     * @param fraudRiskLevel the fraud risk level
     * @return a new TransactionProcessedEvent
     */
    public static TransactionProcessedEvent create(
            String transactionId,
            TransactionStatus status,
            FraudRiskLevel fraudRiskLevel) {

        LocalDateTime now = LocalDateTime.now();

        return TransactionProcessedEvent.builder()
                .transactionId(transactionId)
                .status(status)
                .fraudRiskLevel(fraudRiskLevel)
                .processedAt(now)
                .eventId(UUID.randomUUID().toString())
                .eventTimestamp(now)
                .build();
    }
}
