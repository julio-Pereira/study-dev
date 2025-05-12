package com.bluebank.common.dto;

import com.bluebank.common.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
        * DTO for transaction responses.
        */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private TransactionStatus status;
    private String message;
    private LocalDateTime timestamp;

    /**
     * Factory method for successful transaction creation.
     *
     * @param id the transaction ID
     * @return a success response
     */
    public static TransactionResponse success(String id) {
        return TransactionResponse.builder()
                .id(id)
                .status(TransactionStatus.RECEIVED)
                .message("Transaction received successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Factory method for error responses.
     *
     * @param message the error message
     * @return an error response
     */
    public static TransactionResponse error(String message) {
        return TransactionResponse.builder()
                .status(TransactionStatus.FAILED)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}