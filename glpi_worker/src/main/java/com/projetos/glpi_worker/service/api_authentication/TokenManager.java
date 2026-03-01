package com.projetos.glpi_worker.service.api_authentication;

public interface TokenManager {

    public String getToken(int timeoutSeconds);
    public void authenticate(int timeoutSeconds);

}
