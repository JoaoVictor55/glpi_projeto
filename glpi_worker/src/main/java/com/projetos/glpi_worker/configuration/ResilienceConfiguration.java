package com.projetos.glpi_worker.configuration;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.projetos.glpi_worker.constants.ResilienceConstants;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.Retry;

@Configuration
public class ResilienceConfiguration {


   @Bean
    public RetryRegistry makeRetryRegistryGlpiConfigs(){

        return RetryRegistry.ofDefaults();

    }

    @Bean//caso seja chamado mais de uma vez, o spring retorna um cache :)
    public CircuitBreakerRegistry makeCircuitBreakeregistryGlpiConfigs(){

        return CircuitBreakerRegistry.ofDefaults();

    }

    @Bean
    public CircuitBreaker glpiCircuitBreaker(CircuitBreakerRegistry registry){


        return registry.circuitBreaker(ResilienceConstants.CIRCUIT_BREAKER_NAME_GLPI.name());
    }

    @Bean 
    public Retry glpiRetry(RetryRegistry registry){


        return registry.retry(ResilienceConstants.RETRY_NAME_GLPI.name());

    }


}
