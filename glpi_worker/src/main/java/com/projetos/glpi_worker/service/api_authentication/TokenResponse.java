package com.projetos.glpi_worker.service.api_authentication;

public record TokenResponse(
    String access_token,
    String token_type,
    int expires_in,
    String refresh_token,
    String scope) {

}
