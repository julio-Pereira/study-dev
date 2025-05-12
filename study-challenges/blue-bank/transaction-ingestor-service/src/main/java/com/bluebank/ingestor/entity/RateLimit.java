package com.bluebank.ingestor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rate_limit", schema = "transaction_ingestor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimit {

    @Id
    @Column(length = 100)
    private String key;

    @Column(nullable = false)
    private int count;

    @Column(name = "last_reset", nullable = false)
    private LocalDateTime lastReset;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    /**
     * Increments the count.
     */
    public void increment() {
        this.count++;
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Resets the count.
     */
    public void reset() {
        this.count = 1;
        this.lastReset = LocalDateTime.now();
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Checks if the rate limit needs resetting based on a time window.
     *
     * @param window time window in seconds
     * @return true if reset is needed, false otherwise
     */
    public boolean needsReset(int window) {
        return this.lastReset.plusSeconds(window).isBefore(LocalDateTime.now());
    }
}