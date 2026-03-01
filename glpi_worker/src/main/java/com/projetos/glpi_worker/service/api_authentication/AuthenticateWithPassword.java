package com.projetos.glpi_worker.service.api_authentication;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.projetos.glpi_worker.constants.GlpiAuthParams;

import io.netty.handler.timeout.WriteTimeoutException;

@Component
public class AuthenticateWithPassword implements Authenticate {

    private final GlpiConnectionProperties properties;
    private final WebClient webClient;

    public AuthenticateWithPassword(GlpiConnectionProperties properties) {
        this.properties = properties;
        this.webClient = WebClient.builder()
            .baseUrl(properties.url())
            .build();
    }
    
    
    public TokenResponse authenticate(int timeoutSeconds) throws WebClientException{
        
        TokenResponse tokenResponse = this.webClient.post()
        .uri(properties.apiEndpoint() + GlpiAuthParams.ENDPOINT_AUTH.getValue())
        .body(BodyInserters.fromFormData(GlpiAuthParams.GRANT_TYPE.getValue(), GlpiAuthParams.PASSWORD.getValue())
        .with(GlpiAuthParams.CLIENT_ID.getValue(), properties.clientId())
        .with(GlpiAuthParams.CLIENT_SECRET.getValue(), properties.clientSecret())
        .with(GlpiAuthParams.USERNAME.getValue(), properties.username())
        .with(GlpiAuthParams.PASSWORD.getValue(), properties.password())
        .with(GlpiAuthParams.SCOPE.getValue(), properties.scope()))
        .retrieve()
        .bodyToMono(TokenResponse.class) // O Spring converte o JSON aqui
        .timeout(Duration.ofSeconds(timeoutSeconds))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException("API took too long to reply"))
        .block();

        return tokenResponse;

}


    @Override
    public TokenResponse refreshToken(String refreshToken, int timeoutSeconds) {
        
        var responseToken = this.webClient.post()
        .uri(properties.apiEndpoint() + GlpiAuthParams.ENDPOINT_AUTH.getValue())
        .body(BodyInserters.fromFormData(GlpiAuthParams.GRANT_TYPE.getValue(), GlpiAuthParams.REFRESH_TOKEN_PARAM.getValue())
        .with(GlpiAuthParams.CLIENT_ID.getValue(), properties.clientId())
        .with(GlpiAuthParams.CLIENT_SECRET.getValue(), properties.clientSecret())
        .with(GlpiAuthParams.REFRESH_TOKEN_PARAM.getValue(), refreshToken))
        .retrieve()
        .bodyToMono(TokenResponse.class) // O Spring converte o JSON aqui
        .timeout(Duration.ofSeconds(timeoutSeconds))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException("API took too long to reply"))  
        .block();

        return responseToken;
        
    }

}
