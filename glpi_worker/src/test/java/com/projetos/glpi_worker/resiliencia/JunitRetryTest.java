package com.projetos.glpi_worker.resiliencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

public class JunitRetryTest {

    @Test
    void mustTryAnAumontBeforeFailure(){

        int maxAttemps = 3;
        int waitDurationBetween = 10;

        //tenta uma quantidade máxima de maxAttempts vezes, esperando um tempo entre elas, waitDurationBetween
        RetryConfig config = RetryConfig.custom()
        .maxAttempts(maxAttemps)
        .waitDuration(Duration.ofMillis(waitDurationBetween))
        .build();

        Retry retry = Retry.of("retry-teste", config);

        //conta quantas vezes o código foi executado
        AtomicInteger contador = new AtomicInteger(0);

        assertThrows(RuntimeException.class, ()->{

            retry.executeSupplier(()->{

                contador.incrementAndGet();
                throw new RuntimeException("Falhou");
            });
        });

        assertEquals(maxAttemps, contador.get());
    }

    @Test //mesmo teste do anterior, só que usando o evento
    void mustEmitRetryEvents(){

        Retry retry = Retry.ofDefaults("teste");
        AtomicInteger retryCounter = new AtomicInteger(0);

        retry.getEventPublisher().onRetry(event -> {retryCounter.incrementAndGet();});

        try{
            retry.executeSupplier(() -> {throw new RuntimeException();});
        }
        catch(Exception e){

        }

        assertEquals(2, retryCounter.get());
    }

}
