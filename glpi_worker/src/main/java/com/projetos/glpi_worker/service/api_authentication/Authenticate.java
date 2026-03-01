package com.projetos.glpi_worker.service.api_authentication;

public interface Authenticate {


    TokenResponse authenticate(int timeoutSeconds);
    TokenResponse refreshToken(String refreshToken, int timeoutSeconds);
    


}
