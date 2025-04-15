package com.bluebank.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of possible transaction processing statuses.
 */
@Getter
@RequiredArgsConstructor
public enum TransactionStatus {
        RECEIVED("Received", "Transaction has been received but not yet processed"),
        PROCESSING("Processing", "Transaction is currently being processed"),
        PROCESSED("Processed", "Transaction has been successfully processed"),
        FLAGGED_FOR_REVIEW("Flagged for Review", "Transaction has been flagged for manual review"),
        REJECTED("Rejected", "Transaction was rejected"),
        FAILED("Failed", "Transaction processing failed due to an error");

        private final String description;
        private final String systemDescription;

        /**
         * Checks if the status is a final state.
         *
         * @return true if the status is a final state, false otherwise
         */
        public boolean isFinalState() {
            return this == PROCESSED || this == REJECTED || this == FAILED;
        }

        /**
         * Checks if the status indicates a problem with the transaction.
         *
         * @return true if the status indicates a problem, false otherwise
         */
        public boolean isProblem() {
            return this == FLAGGED_FOR_REVIEW || this == REJECTED || this == FAILED;
        }
}