package com.bluebank.ingestor;

import com.bluebank.common.model.Currency;
import com.bluebank.common.model.TransactionStatus;
import com.bluebank.common.model.TransactionType;
import com.bluebank.ingestor.entity.TransactionAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAuditResponse {

    private Long id;
    private String transactionId;
    private String accountId;
    private BigDecimal amount;
    private Currency currency;
    private TransactionType type;
    private String merchantName;
    private String merchantCategory;
    private LocalDateTime transactionTimestamp;
    private LocalDateTime createdAt;
    private String pubsubMessageId;
    private TransactionStatus status;
    private String errorMessage;

    /**
     * Converts a TransactionAudit entity to a response DTO.
     *
     * @param entity the entity to convert
     * @return the response DTO
     */
    public static TransactionAuditResponse fromEntity(TransactionAudit entity) {
        return TransactionAuditResponse.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .accountId(entity.getAccountId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .type(entity.getType())
                .merchantName(entity.getMerchantName())
                .merchantCategory(entity.getMerchantCategory())
                .transactionTimestamp(entity.getTransactionTimestamp())
                .createdAt(entity.getCreatedAt())
                .pubsubMessageId(entity.getPubsubMessageId())
                .status(entity.getStatus())
                .errorMessage(entity.getErrorMessage())
                .build();
    }

}
