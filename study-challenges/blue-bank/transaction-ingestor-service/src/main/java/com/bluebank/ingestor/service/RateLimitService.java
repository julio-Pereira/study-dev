package com.bluebank.ingestor.service;

import com.bluebank.ingestor.entity.RateLimit;
import com.bluebank.ingestor.repository.RateLimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimitService {

    private final RateLimitRepository rateLimitRepository;

    @Value("${rate-limit.transaction-per-minute}")
    private int transactionsPerMinute;

    @Value("${rate-limit.window-seconds}")
    private int windowSeconds;

    @Transactional
    public boolean checkAndUpdateRateLimit(String accountId) {
        String key = "transaction_" + accountId;
        Optional<RateLimit> rateLimitOpt = rateLimitRepository.findByKey(key);
        RateLimit rateLimit;

        if (rateLimitOpt.isPresent()) {
            rateLimit = rateLimitOpt.get();
             if (rateLimit.needsReset(windowSeconds)) {
                 rateLimit.reset();
                 log.debug("Rate limit reset for account: {}", accountId);
                 rateLimitRepository.save(rateLimit);
                 return true;
             }

            if (rateLimit.getCount() >= transactionsPerMinute) {
                log.warn("Ralet limit exceeded for account: {}", accountId);
                return false;
            }
            rateLimit.increment();
        } else {
            LocalDateTime now = LocalDateTime.now();
            rateLimit = RateLimit.builder()
                    .key(key)
                    .count(1)
                    .lastReset(now)
                    .lastUpdate(now)
                    .build();
        }

        rateLimitRepository.save(rateLimit);
        return true;
    }
}
