package com.projetos.glpi_worker.service.api_authentication;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshingTokenManager implements TokenManager {

    @Autowired
    private Authenticate authenticate;

    private volatile TokenResponse tokenResponse;
    private volatile Instant expiryTime;
    private final Object lock = new Object();
    
    public String getToken(int timeoutSeconds) {
        if (!isTokenValid()) {
            ensureAuthenticated(timeoutSeconds, true);
        }
        return tokenResponse.access_token();
    }

    public void authenticate(int timeoutSeconds) {
        if (!isTokenValid()) {
            ensureAuthenticated(timeoutSeconds, false);
        }
    }


    private void ensureAuthenticated(int timeoutSeconds, boolean useRefresh) {
        synchronized (lock) {
            // Re-checa dentro do lock (Double-Checked Locking)
            if (!isTokenValid()) {
                TokenResponse response;
                
                if (useRefresh && tokenResponse != null) {
                    response = authenticate.refreshToken(tokenResponse.access_token(), timeoutSeconds);
                } else {
                    response = authenticate.authenticate(timeoutSeconds);
                }

                // Cálculo local para garantir atomicidade na visão das outras threads
                Instant expiration = Instant.now().plusSeconds(response.expires_in());

                // Ordem importa: primeiro o que completa o estado, por último o volatile principal
                this.expiryTime = expiration;
                this.tokenResponse = response;
            }
        }
    }

    private boolean isTokenValid() {
        // volatile: leitura atômica e visibilidade garantida
        //entre as threads.
        //internamente, a variável não é "cacheada" localmente (na tread); mas atualizada diretamente na memória principal, garantindo que todas as threads vejam a versão mais recente.
        //e volatile é atômico para tipos primitivos, exceto 
        //em operações compostas
        return tokenResponse != null && expiryTime != null && 
               Instant.now().isBefore(expiryTime.minusSeconds(60));
    }
}