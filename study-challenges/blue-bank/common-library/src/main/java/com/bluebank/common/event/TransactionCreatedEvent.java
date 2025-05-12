package com.bluebank.common.event;


import com.bluebank.common.model.Currency;
import com.bluebank.common.model.TransactionStatus;
import com.bluebank.common.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a new transaction is created.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreatedEvent {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private Currency currency;
    private TransactionType type;
    private String merchantName;
    private String merchantCategory;
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private String eventId;
    private LocalDateTime eventTimestamp;
}
