package com.projetos.glpi_worker.service.connection;

public interface Authenticate {

    String getToken();
    String getTokenType();
    Integer getExpiresIn();
    String getRefreshToken();
    void authenticate();

}
