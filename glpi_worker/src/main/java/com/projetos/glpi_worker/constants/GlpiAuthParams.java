package com.projetos.glpi_worker.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlpiAuthParams {

    GRANT_TYPE("grant_type"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    USERNAME("username"),
    PASSWORD("password"),
    REFRESH_TOKEN_PARAM("refresh_token"),
    SCOPE("scope"),
    ENDPOINT_AUTH("/token"),
    ENDPOINT_REVOKE("/revoke"),
    TOKEN_REVOKE("token");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}