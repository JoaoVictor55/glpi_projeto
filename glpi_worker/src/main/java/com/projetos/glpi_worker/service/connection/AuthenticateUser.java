package com.projetos.glpi_worker.service.connection;

import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

public class AuthenticateUser {

    private final String URL;
    private final String CLIENTE_ID;
    private final String CLIENT_SECRET;
    private final String USERNAME;
    private final String PASSWORD;
    private final String SCOPE;
    private final WebClient webClient;
    private TokenResponse tokenResponse;

    public AuthenticateUser(String URL, 
        String CLIENTE_ID, String CLIENT_SECRET, String USERNAME, String PASSWORD, String SCOPE) {

        this.URL = URL;
        this.CLIENTE_ID = CLIENTE_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.SCOPE = SCOPE;
        
        this.webClient = WebClient.builder()
            .baseUrl(URL)
            .build();

    }

    public String getToken() {
        
        if (tokenResponse == null) {
            authenticate();
        }
        return tokenResponse.access_token();
    }

    public String getTokenType() {
        if (tokenResponse == null) {
            authenticate();
        }
        return tokenResponse.token_type();
    }

    public int getExpiresIn() {
        if (tokenResponse == null) {
            authenticate();
        }
        return tokenResponse.expires_in();
    }

    public String getRefreshToken() {
        if (tokenResponse == null) {
            authenticate();
        }
        return tokenResponse.refresh_token();
    }

    
    public void authenticate() {
        
        try{
            
        TokenResponse response = this.webClient.post()
        .uri("/api.php/token")
        .body(BodyInserters.fromFormData("grant_type", "password")
                .with("client_id", CLIENTE_ID)
                .with("client_secret", CLIENT_SECRET)
                .with("username", USERNAME)
                .with("password", PASSWORD)
                .with("scope", SCOPE))
        .retrieve()
        .bodyToMono(TokenResponse.class) // O Spring converte o JSON aqui
        .block(); // Espera a resposta (Síncrono para o Shell)

            System.out.println("Authentication successful!");
        }catch(WebClientException e){

            System.err.print("Error during authentication: "+e.getMessage());
        }
        catch(Exception e){
            System.err.print("Unexpected error: "+e.getMessage());
        }


    }
}
