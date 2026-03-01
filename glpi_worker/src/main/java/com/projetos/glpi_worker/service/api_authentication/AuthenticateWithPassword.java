package com.projetos.glpi_worker.service.api_authentication;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.projetos.glpi_worker.constants.GlpiConstants;

import io.netty.handler.timeout.WriteTimeoutException;

@Component
public class AuthenticateWithPassword implements Authenticate {

    private final GlpiConnectionProperties properties;
    private final WebClient webClient;
    private final GlpiConstants.ParamsPasswordAuth paramsAuth = new GlpiConstants.ParamsPasswordAuth();

    public AuthenticateWithPassword(GlpiConnectionProperties properties) {
        this.properties = properties;
        this.webClient = WebClient.builder()
            .baseUrl(properties.url())
            .build();
    }
    
    
    public TokenResponse authenticate(int timeoutSeconds) throws WebClientException{
        
        TokenResponse tokenResponse = this.webClient.post()
        .uri(properties.apiEndpoint()+paramsAuth.getApiEndpointAuth())
        .body(BodyInserters.fromFormData(paramsAuth.getGrantType(), paramsAuth.getGrantTypePassword())
                .with(paramsAuth.getClientId(), properties.clientId())
                .with(paramsAuth.getClientSecret(), properties.clientSecret())
                .with(paramsAuth.getUsername(), properties.username())
                .with(paramsAuth.getGrantTypePassword(), properties.password())
                .with(paramsAuth.getScope(), properties.scope()))
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
        .uri(properties.apiEndpoint()+paramsAuth.getApiEndpointAuth())
        .body(BodyInserters.fromFormData(paramsAuth.getGrantType(), paramsAuth.getGrantTypeRefresh())
                .with(paramsAuth.getClientId(), properties.clientId())
                .with(paramsAuth.getClientSecret(), properties.clientSecret())
                .with(paramsAuth.getRefreshToken(), refreshToken))
        .retrieve()
        .bodyToMono(TokenResponse.class) // O Spring converte o JSON aqui
        .timeout(Duration.ofSeconds(timeoutSeconds))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException("API took too long to reply"))  
        .block();

        return responseToken;
        
    }

}
