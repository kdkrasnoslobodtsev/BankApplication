package com.example.models;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
public class Retries extends Retry {
    private final Integer maxRetries;
    private final List<Duration> retryDelays;

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(this::getRetryDelay);
    }

    private Mono<Long> getRetryDelay(Retry.RetrySignal retrySignal) {
        if (retrySignal.totalRetries() < maxRetries) {
            return Mono.delay(retryDelays.get(Integer.parseInt(String.valueOf(retrySignal.totalRetries())))).thenReturn(retrySignal.totalRetries());
        } else {
            throw Exceptions.propagate(retrySignal.failure());
        }
    }
}
