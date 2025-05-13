package com.bluebank.ingestor.controller;

import com.bluebank.common.dto.TransactionRequest;
import com.bluebank.common.dto.TransactionResponse;
import com.bluebank.ingestor.service.TransactionService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Transactions", description = "API for creating financial transactions")
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    @Timed(value = "api.transaction.create", description = "Time taken to process a transaction")
    @Operation(
            summary = "Create a new transaction",
            description = "Creates a new financial transaction and publishes it for processing",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Transaction accepted for processing"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
            }
    )
    public ResponseEntity<TransactionResponse> receiveTransaction(
            @Valid @RequestBody TransactionRequest request) {
        log.info("Received transaction request: {}", request);
        TransactionResponse response = service.processTransaction(request);
        log.info("Transaction processed successfully: {}", response);
        return ResponseEntity.accepted().body(response);
    }
}
