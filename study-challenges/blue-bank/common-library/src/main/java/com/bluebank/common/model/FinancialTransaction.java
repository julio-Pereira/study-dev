package com.bluebank.common.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialTransaction {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private String merchantName;
    private String merchantCategory;
    private LocalDateTime timestamp;
    private TransactionStatus status;

    @Builder.Default
    private Map<String, Object> additionalData = new HashMap<>();

}
