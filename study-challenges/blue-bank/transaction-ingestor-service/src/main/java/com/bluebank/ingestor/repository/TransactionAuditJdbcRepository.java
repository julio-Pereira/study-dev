package com.bluebank.ingestor.repository;

import com.bluebank.ingestor.entity.TransactionAudit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * JDBC implementation for bulk operations on TransactionAudit.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionAuditJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Saves a batch of transaction audits for improved performance.
     *
     * @param audits list of transaction audits to save
     * @return the number of records saved
     */
    public int saveBatch(List<TransactionAudit> audits) {
        if (audits == null || audits.isEmpty()) {
            return 0;
        }

        String sql = "INSERT INTO transaction_ingestor.transactions_audit " +
                "(transaction_id, account_id, amount, currency, type, merchant_name, " +
                "merchant_category, transaction_timestamp, pubsub_message_id, status, error_message) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int[][] rows = jdbcTemplate.batchUpdate(
                sql,
                audits,
                audits.size(),
                (ps, audit) -> {
                    ps.setString(1, audit.getTransactionId());
                    ps.setString(2, audit.getAccountId());
                    ps.setBigDecimal(3, audit.getAmount());
                    ps.setString(4, audit.getCurrency().toString());
                    ps.setString(5, audit.getType().toString());
                    ps.setString(6, audit.getMerchantName());
                    ps.setString(7, audit.getMerchantCategory());
                    ps.setTimestamp(8, Timestamp.valueOf(audit.getTransactionTimestamp()));
                    ps.setString(9, audit.getPubsubMessageId());
                    ps.setString(10, audit.getStatus().toString());
                    ps.setString(11, audit.getErrorMessage());
                });

        int totalInserted = 0;

        for (int[] row : rows) {
            totalInserted += row[0];
        }

        log.debug("Batch saved {} transaction audits", totalInserted);
        return totalInserted;
    }
}