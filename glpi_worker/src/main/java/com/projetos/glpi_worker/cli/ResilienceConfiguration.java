package com.projetos.glpi_worker.cli;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.Retry;

@Configuration
public class ResilienceConfiguration {

    @Bean
    public CircuitBreaker glpiCircuitBreaker(){

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();


        return registry.circuitBreaker("glpiCircuitBreaker");
    }

    @Bean 
    public Retry glpiRetry(){

        RetryConfig config = RetryConfig.custom()
        .maxAttempts(3)
        .waitDuration(Duration.ofMillis(500))
        .retryExceptions(IOException.class, TimeoutException.class)
        .ignoreExceptions(NullPointerException.class)
        .build();


        RetryRegistry retryRegistry = RetryRegistry.of(config);

        return retryRegistry.retry("glpiRetry");

    }

}
