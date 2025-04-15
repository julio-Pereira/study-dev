package com.bluebank.common.model;

/**
 * Enumeration of possible transaction processing statuses.
 */
public enum TransactionStatus {
        RECEIVED,
        PROCESSING,
        PROCESSED,
        FLAGGED_FOR_REVIEW,
        REJECTED,
        FAILED
}