package com.bluebank.common.exception;

/**
 * Exception thrown when transaction processing fails.
 */
public class TransactionProcessingException extends BusinessException {
    private static final String ERROR_CODE = "TRANSACTION_PROCESSING_ERROR";

    public TransactionProcessingException(String message) {
        super(message, ERROR_CODE);
    }

    public TransactionProcessingException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }

    public TransactionProcessingException(String transactionId, String reason) {
        super(String.format("Failed to process transaction %s: %s", transactionId, reason), ERROR_CODE);
    }
}
