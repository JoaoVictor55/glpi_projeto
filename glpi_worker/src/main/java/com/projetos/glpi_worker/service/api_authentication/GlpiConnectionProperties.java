package com.projetos.glpi_worker.service.api_authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "glpi")
public record GlpiConnectionProperties(
    String url,
    String clientId,
    String clientSecret,
    String username,
    String password,
    String scope,
    String apiEndpoint

) {

}
