package com.bluebank.ingestor.controller;

import com.bluebank.ingestor.TransactionAuditResponse;
import com.bluebank.ingestor.entity.TransactionAudit;
import com.bluebank.ingestor.repository.TransactionAuditRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/audits")
@RequiredArgsConstructor
@Slf4j
public class TransactionAuditController {

    private final TransactionAuditRepository auditRepository;

    /**
     * Endpoint to get transaction audits by account ID.
     *
     * @param accountId the account ID
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated transaction audit data
     */
    @GetMapping("/accounts/{accountId}")
    @Timed(value = "api.audit.byAccount", description = "Time taken to fetch transaction audits by account")
    public ResponseEntity<Page<TransactionAuditResponse>> getAuditsByAccount(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Fetching transaction audits for account: {}", accountId);

        Page<TransactionAudit> audits = auditRepository.findByAccountIdOrderByCreatedAtDesc(accountId, PageRequest.of(page, size));

        Page<TransactionAuditResponse> response = audits.map(TransactionAuditResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Timed(value = "api.audit.byDateRange", description = "Time taken to fetch audits by date range")
    public ResponseEntity<Page<TransactionAuditResponse>> getAuditsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Fetching transaction audits between {} and {}", startDate, endDate);

        Page<TransactionAudit> audits = auditRepository.findByDateRange(
                startDate,
                endDate,
                PageRequest.of(page, size));

        Page<TransactionAuditResponse> response = audits.map(TransactionAuditResponse::fromEntity);

        return ResponseEntity.ok(response);
    }
}
