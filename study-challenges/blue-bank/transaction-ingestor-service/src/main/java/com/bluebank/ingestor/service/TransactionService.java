package com.bluebank.ingestor.service;

import com.bluebank.common.dto.TransactionRequest;
import com.bluebank.common.dto.TransactionResponse;
import com.bluebank.common.exception.ValidationException;
import com.bluebank.common.model.TransactionStatus;
import com.bluebank.ingestor.entity.TransactionAudit;
import com.bluebank.ingestor.repository.TransactionAuditJdbcRepository;
import com.bluebank.ingestor.repository.TransactionAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionPublisherService publisherService;
    private final TransactionAuditRepository auditRepository;
    private final TransactionAuditJdbcRepository auditJdbcRepository;
    private final RateLimitService rateLimitService;

    @Transactional
    public TransactionResponse processTransaction(TransactionRequest request) {
        validateTransaction(request);

        checkRateLimits(request.getAccountId());

        String transactionId = UUID.randomUUID().toString();

        try {
            String messageId = publisherService.publishTransaction(request);

            saveTransactionAudit(transactionId, request, messageId, null);

            return TransactionResponse.builder()
                    .id(transactionId)
                    .status(TransactionStatus.RECEIVED)
                    .message("Transaction received successfuly")
                    .timestamp(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("Failed to process transaction", e);

            saveTransactionAudit(transactionId, request, null, e.getMessage());

            return TransactionResponse.builder()
                    .id(transactionId)
                    .status(TransactionStatus.FAILED)
                    .message("Failed to process transaction: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Validates the transaction request.
     *
     * @param request the transaction request
     * @throws ValidationException if validation fails
     */
    private void validateTransaction(TransactionRequest request) {
        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new ValidationException("amount", "Amount must be greater than zero");
        }

        if (request.getCurrency() == null) {
            throw new ValidationException("currency", "Currency is required");
        }

        if (request.getType() == null) {
            throw new ValidationException("type", "Transaction type is required");
        }

        if (request.getAccountId() == null || request.getAccountId().isBlank()) {
            throw new ValidationException("accountId", "Account ID is required");
        }
    }

    /**
     * Checks rate limits for the account.
     *
     * @param accountId the account ID
     * @throws ValidationException if rate limit is exceeded
     */
    private void checkRateLimits(String accountId) {
        boolean withinLimit = rateLimitService.checkAndUpdateRateLimit(accountId);
        if (!withinLimit) {
            throw new ValidationException("Too many requests for this account. Please try again later.");
        }
    }

    /**
    * Saves an audit record for the transaction.
    *
    * @param transactionId the transaction ID
     * @param request the transaction request
     * @param messageId the PubSub message ID (null if failed)
     * @param errorMessage the error message (null if successful)
     */
    private void saveTransactionAudit(String transactionId, TransactionRequest request,
                                      String messageId, String errorMessage) {
        LocalDateTime timestamp = request.getTimestamp() != null ?
                request.getTimestamp() : LocalDateTime.now();

        TransactionAudit audit;
        if (errorMessage == null) {
            audit = TransactionAudit.createSuccess(
                    transactionId,
                    request.getAccountId(),
                    request.getAmount(),
                    request.getCurrency(),
                    request.getType(),
                    request.getMerchantName(),
                    request.getMerchantCategory(),
                    timestamp,
                    messageId
            );
        } else {
            audit = TransactionAudit.createFailure(
                    transactionId,
                    request.getAccountId(),
                    request.getAmount(),
                    request.getCurrency(),
                    request.getType(),
                    request.getMerchantName(),
                    request.getMerchantCategory(),
                    timestamp,
                    errorMessage
            );
        }

        auditRepository.save(audit);
    }
}
