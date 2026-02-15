package com.projetos.glpi_worker.service.connection;

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
        if (!isTokenValid()) {
            TokenResponse response;
            try {
                if (useRefresh && tokenResponse != null) {
                    // Tenta atualizar
                    response = authenticate.refreshToken(tokenResponse.access_token(), timeoutSeconds);
                } else {
                    // Login inicial
                    response = authenticate.authenticate(timeoutSeconds);
                }
            } catch (Exception e) {
                // Se o refresh falhar 
                if (useRefresh) {
                    
                    response = authenticate.authenticate(timeoutSeconds); 
                } else {
                    
                    throw e; 
                }
            }

            // Atualização atômica do estado
            Instant expiration = Instant.now().plusSeconds(response.expires_in());
            this.expiryTime = expiration;
            this.tokenResponse = response;
        }
    }
}

private boolean isTokenValid() {
        // Como ambos são volatile, a leitura aqui é thread-safe
        return tokenResponse != null && expiryTime != null && 
               Instant.now().isBefore(expiryTime.minusSeconds(60));
    }
}