package com.projetos.glpi_worker.Constants;

import lombok.Getter;

public class GlpiConstants {

    //nome dos parâmetros para autenticação
    @Getter
    public static class ParamsPasswordAuth {

        
        private String grantType = "grant_type";
        private String clientId = "client_id";
        private String clientSecret = "client_secret";
        private String username = "username";
        private String password = "password";
        private String scope = "scope";
        private String apiEndpointAuth = "/token";
    }

}
