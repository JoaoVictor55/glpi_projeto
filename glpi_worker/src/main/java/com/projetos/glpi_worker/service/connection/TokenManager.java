package com.projetos.glpi_worker.service.connection;

public interface TokenManager {

    public String getToken(int timeoutSeconds);
    public void authenticate(int timeoutSeconds);

}
