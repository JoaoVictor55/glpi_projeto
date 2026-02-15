package com.projetos.glpi_worker.service.connection;

import java.time.Duration;

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

import com.projetos.glpi_worker.Constants.GlpiConstants;

import io.netty.handler.timeout.WriteTimeoutException;

@Component
public class AuthenticateWithPassword implements Authenticate {

    private final GlpiConnectionProperties properties;
    private final WebClient webClient;
    private TokenResponse tokenResponse;
    private final GlpiConstants.ParamsPasswordAuth paramsAuth = new GlpiConstants.ParamsPasswordAuth();
    private final String grantTypePassword = "password";

    public AuthenticateWithPassword(GlpiConnectionProperties properties) {
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
        .uri(properties.apiEndpoint()+paramsAuth.getApiEndpointAuth())
        .body(BodyInserters.fromFormData(paramsAuth.getGrantType(), grantTypePassword)
                .with(paramsAuth.getClientId(), properties.clientId())
                .with(paramsAuth.getClientSecret(), properties.clientSecret())
                .with(paramsAuth.getUsername(), properties.username())
                .with(paramsAuth.getPassword(), properties.password())
                .with(paramsAuth.getScope(), properties.scope()))
        .retrieve()
        .bodyToMono(TokenResponse.class) // O Spring converte o JSON aqui
        .timeout(Duration.ofSeconds(2))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException("API took too long to reply"))
        .block();

}
}
