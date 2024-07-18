package com.example.accounts.utils;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiterGenerator {
    public RateLimiter generate(Integer customerId) {
        return RateLimiter.of(customerId.toString(), RateLimiterConfig
                .custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(0))
                .build()
        );
    }
}
