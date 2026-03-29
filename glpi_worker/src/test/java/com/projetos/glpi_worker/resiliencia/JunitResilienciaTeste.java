package com.projetos.glpi_worker.resiliencia;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

public class JunitResilienciaTeste {


    @Test
    void mustFailWhenFailureLimitIsReached(){

        //quantas chamadas a classe olha
        int slidingWindowSize = 2;
        //porcentagem de falhas que ele tolera
        int failureRateThreshold = 50;

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
        .failureRateThreshold(failureRateThreshold)
        .slidingWindowSize(slidingWindowSize)
        .build();

        CircuitBreaker cb = CircuitBreaker.of("teste", config);

        assertEquals(CircuitBreaker.State.CLOSED,  cb.getState());//circuito criado, ele está fechado (operante)

        cb.onError(0, java.util.concurrent.TimeUnit.NANOSECONDS, new RuntimeException());

        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());//primeiro err

        cb.onError(0, java.util.concurrent.TimeUnit.NANOSECONDS, new RuntimeException());

        assertEquals(CircuitBreaker.State.OPEN, cb.getState()); //ocorreu o segundo, ultrapassando o teto, fazendo o circuito desarmar.
    }

    @Test
    void mustChangeToHalfOpenAfterWaitingTime() throws InterruptedException {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .waitDurationInOpenState(Duration.ofMillis(100)) // Espera só 100ms
            .build();

        CircuitBreaker cb = CircuitBreaker.of("teste", config);

        // Forçamos a abertura (método utilitário para testes)
        cb.transitionToOpenState();
    
        // Esperamos o tempo configurado
        Thread.sleep(150); 

        // O CircuitBreaker não muda sozinho para Half-Open parado. 
        // Ele muda na próxima tentativa de chamada!
        cb.tryAcquirePermission(); 

        assertEquals(CircuitBreaker.State.HALF_OPEN, cb.getState());
    }
    
    
}