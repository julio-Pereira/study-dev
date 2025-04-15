package com.bluebank.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of possible financial transaction types.
 */
@Getter
@RequiredArgsConstructor
public enum TransactionType {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    BANK_TRANSFER("Bank Transfer"),
    PIX("PIX"),
    WITHDRAWAL("Withdrawal"),
    DEPOSIT("Deposit"),
    LOAN_PAYMENT("Loan Payment"),
    FEE("Fee"),
    INTEREST("Interest"),
    OTHER("Other");

    private final String description;

    /**
     * Checks if the transaction type is a payment method (credit card, debit card, etc.).
     *
     * @return true if it's a payment method, false otherwise
     */
    public boolean isPaymentMethod() {
        return this == CREDIT_CARD || this == DEBIT_CARD || this == PIX;
    }

    /**
    * Checks if the transaction represents a fee or charge.
    *
    * @return true if it's a fee or charge, false otherwise
    */
    public boolean isFeeOrCharge() {
        return this == FEE || this == INTEREST;
    }
}
