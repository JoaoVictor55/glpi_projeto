package com.projetos.glpi_worker.service.connection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Component
public class AuthenticateUser implements Authenticate {

    private final GlpiConnectionProperties properties;
    private final WebClient webClient;
    private TokenResponse tokenResponse;


    public AuthenticateUser(GlpiConnectionProperties properties) {
        this.properties = properties;
        this.webClient = WebClient.builder()
            .baseUrl(properties.url())
            .build();
    }
    


    public String getToken() {
    
        return (tokenResponse == null) ? null : tokenResponse.access_token();
    }

    public String getTokenType() {
        
        return (tokenResponse == null) ? null : tokenResponse.token_type();
    }

    public Integer getExpiresIn() {

        return (tokenResponse == null) ? null : tokenResponse.expires_in();
    }

    public String getRefreshToken() {
        
        return (tokenResponse == null) ? null : tokenResponse.refresh_token();
    }

    
    public void authenticate() throws WebClientException{
        
        tokenResponse = this.webClient.post()
        .uri("/api.php/token")
        .body(BodyInserters.fromFormData("grant_type", "password")
                .with("client_id", properties.clientId())
                .with("client_secret", properties.clientSecret())
                .with("username", properties.username())
                .with("password", properties.password())
                .with("scope", properties.scope()))
        .retrieve()
        .bodyToMono(TokenResponse.class) // O Spring converte o JSON aqui
        .block();
}
}
