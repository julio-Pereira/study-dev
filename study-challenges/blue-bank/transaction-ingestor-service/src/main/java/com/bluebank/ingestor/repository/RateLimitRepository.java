package com.bluebank.ingestor.repository;

import com.bluebank.ingestor.entity.RateLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit, String> {
    /**
     * Finds a rate limit by key.
     */
    Optional<RateLimit> findByKey(String key);
}
