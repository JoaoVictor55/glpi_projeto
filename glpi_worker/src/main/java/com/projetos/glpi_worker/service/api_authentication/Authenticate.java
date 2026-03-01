package com.projetos.glpi_worker.service.api_authentication;

import ch.qos.logback.core.subst.Token;

public interface Authenticate {


    TokenResponse authenticate(int timeoutSeconds);
    TokenResponse refreshToken(String refreshToken, int timeoutSeconds);
    


}
