package com.bluebank.ingestor.repository;

import com.bluebank.ingestor.entity.TransactionAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionAuditRepository extends JpaRepository<TransactionAudit, Long> {
    /**
     * Finds audits by account ID.
     */
    Page<TransactionAudit> findByAccountIdOrderByCreatedAtDesc(String accountId, Pageable pageable);

    /**
     * Finds audits by transaction ID.
     */
    List<TransactionAudit> findByTransactionId(String transactionId);

    /**
     * Finds audits within a date range.
     */
    @Query("SELECT ta FROM TransactionAudit ta WHERE ta.createdAt BETWEEN :startDate AND :endDate ORDER BY ta.createdAt DESC")
    Page<TransactionAudit> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    /**
     * Counts transactions by account in a time period.
     */
    @Query("SELECT COUNT(ta) FROM TransactionAudit ta WHERE ta.accountId = :accountId AND ta.createdAt >= :since")
    int countRecentTransactionsByAccount(@Param("accountId") String accountId, @Param("since") LocalDateTime since);
}
