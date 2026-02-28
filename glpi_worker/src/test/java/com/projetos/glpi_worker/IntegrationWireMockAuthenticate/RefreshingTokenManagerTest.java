package com.projetos.glpi_worker.IntegrationWireMockAuthenticate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projetos.glpi_worker.service.connection.Authenticate;
import com.projetos.glpi_worker.service.connection.RefreshingTokenManager;
import com.projetos.glpi_worker.service.connection.TokenResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class RefreshingTokenManagerTest {

    @Mock
    private Authenticate authenticate;

    @InjectMocks
    private RefreshingTokenManager tokenManager;//injete o mock (atributo anotado com mock) aqui

    @Test
    public void authenticateWhenNoToken(){
        
        TokenResponse mockTokenResponse = new TokenResponse("mock-token", "access-token", 100, "refresh-mock-token","mock-scope");

        when(authenticate.authenticate(anyInt())).thenReturn(mockTokenResponse);

        String token = tokenManager.getToken(10);

        assertEquals(mockTokenResponse.access_token(), token);

    }

    @Test
    public void notAuthenticateWhenToken(){

        TokenResponse mockTokenResponse = new TokenResponse("mock-token", "access-token", 100, "refresh-mock-token","mock-scope");

        when(authenticate.authenticate(anyInt())).thenReturn(mockTokenResponse);

        tokenManager.getToken(10); //faz uma chamada

        String token = tokenManager.getToken(10); //fazemos algumas chamadas 

        String token2 = tokenManager.getToken(13);

        String token3 = tokenManager.getToken(11);

        //os tokens precisam ser iguais e...
        assertEquals(mockTokenResponse.access_token(), token);
        assertEquals(mockTokenResponse.access_token(), token2);
        assertEquals(mockTokenResponse.access_token(), token3);

        //...a função authenticate precisa ter sido chamada apenas uma ver
        verify(authenticate, times(1)).authenticate(anyInt());

    }

    @Test
    public void authenticateMultiThread() throws InterruptedException{

        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numThreads);

        TokenResponse mockTokenResponse = new TokenResponse("mock-token", "access-token", 100, "refresh-mock-token","mock-scope");

        when(authenticate.authenticate(anyInt())).thenReturn(mockTokenResponse);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // Todas as threads param aqui e esperam o sinal
                    tokenManager.getToken(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        finishLatch.await(5, TimeUnit.SECONDS);
        verify(authenticate, times(1)).authenticate(anyInt());
        executor.shutdown();

    }

}
