package com.bluebank.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String id;
    private String customerId;
    private String accountNumber;
    private AccountType type;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum AccountType {
        CHECKING,
        CREDIT,
        LOAN,
        INVESTMENT,
        SAVINGS
    }

    public enum AccountStatus {
        ACTIVE,
        BLOCKED,
        CLOSED,
        INACTIVE
    }


    /**
     * Factory method to create a new account.
     *
     * @param customerId the customer ID
     * @param accountNumber the account number
     * @param type the account type
     * @param currency the account currency
     * @return a new Account with default values
     */
    public static Account createNew(
            String customerId,
            String accountNumber,
            AccountType type,
            Currency currency
    ) {
        LocalDateTime now = LocalDateTime.now();
        return Account.builder()
                .id(UUID.randomUUID().toString())
                .customerId(customerId)
                .accountNumber(accountNumber)
                .type(type)
                .status(AccountStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .currency(currency)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Adjusts the account balance by the specified amount.
     *
     * @param amount the amount to adjust by (positive for credit, negative for debit)
     */
    public void adjustBalance(BigDecimal amount) {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot adjust balance of an inactive account");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the account has sufficient funds for a debit operation.
     *
     * @param amount the amount to check
     * @return true if sufficient funds are available, false otherwise
     */
    public boolean hasSufficientFunds(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }



}
