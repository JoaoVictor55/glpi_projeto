package com.projetos.glpi_worker.service.connection;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component //por padrão; Components são singletons
public class TokenManager {

    
    @Autowired
    private Authenticate authenticate;

    private volatile TokenResponse tokenResponse;
    private Instant expiryTime;
    private final Object lock = new Object();

    public String getToken() {
        
        if(!isTokenValid()){

            synchronized(lock){

                if(!isTokenValid()){
                    refreshToken();
                }
            }

        }

        return tokenResponse.access_token();
    }

    public void authenticate(int timeoutSeconds){ 
        this.tokenResponse = authenticate.authenticate(timeoutSeconds);
        this.expiryTime = Instant.now().plusSeconds(tokenResponse.expires_in());
    }

    private void refreshToken() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
    }

    private boolean isTokenValid(){

        //adicionando uma margem de 60 segundos
        return tokenResponse != null && expiryTime != null && Instant.now().isBefore(expiryTime.minusSeconds(60));
    }
}
