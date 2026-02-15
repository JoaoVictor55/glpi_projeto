package com.projetos.glpi_worker.IntegrationWireMockAuthenticate;


import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientException;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.projetos.glpi_worker.service.connection.AuthenticateUser;
import com.projetos.glpi_worker.service.connection.GlpiConnectionProperties;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

//este é um teste de integração usando o WireMock sem o Spring; assim não está sendo considerado todo o contexto do Spring, apenas a //classe AuthenticateUser e suas dependências diretas

@WireMockTest(httpPort = 8081)
public class AuthenticationUserWireMockTest {

    private AuthenticateUser authenticateUser;

    @BeforeEach
    void setup() {
        
        GlpiConnectionProperties config = new GlpiConnectionProperties(

            "http://localhost:8081",  // URL do WireMock
            "test-client-id",
            "test-client-secret",
            "test-user",
            "test-pass",
            "test-scope"
        );
        
        authenticateUser = new AuthenticateUser(config);
    }

    @Test
    void testAuthenticationWithWireMock(){

        //criamos um stub; um endpoint falso para simular a resposta da autenticação
        //o stub irá interceptar a requisição POST para /api.php/token e retornar um token falso
        //requisição POST para /api.php/token com grant_type=password, retornarão uma resposta com 
        //status 200 e um corpo JSON contendo um token de acesso falso
        stubFor(post(urlEqualTo("/api.php/token"))
        .withRequestBody(containing("grant_type=password"))
        .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"access_token\":\"mock-token\",\"token_type\":\"Bearer\", \"expires_in\":3600}"))
        );

        authenticateUser.authenticate();

        assert authenticateUser.getToken() != null && authenticateUser.getToken().equals("mock-token") : "Token de autenticação deve ser 'mock-token'";

    }

    @Test 
    void testAuthenticationFailureWithWireMock(){

        //criamos um stub para simular uma falha de autenticação
        //requisição POST para /api.php/token com grant_type=password, retornarão uma resposta com 
        //status 401 e um corpo JSON contendo uma mensagem de erro
        stubFor(post(urlEqualTo("/api.php/token"))
        .withRequestBody(containing("grant_type=password"))
        .willReturn(aResponse()
        .withStatus(401)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"error\":\"invalid_client\",\"error_description\":\"Client authentication failed\"}"))
        );

        try{

            authenticateUser.authenticate();
        }
        catch(WebClientException e){

            assert authenticateUser.getTokenType() == null : "Token type deve ser null em caso de falha";
            assert authenticateUser.getToken() == null : "Token de autenticação deve ser null em caso de falha";
            assert authenticateUser.getExpiresIn() == null : "Expires in deve ser 0 em caso de falha";
            assert authenticateUser.getRefreshToken() == null : "Refresh token deve ser null em caso de falha"; 
        }

    }

    @Test
    void testAuthenticationDelayResponse(){

        stubFor(post(urlEqualTo("/api.php/token"))
        .withRequestBody(containing("grant_type=password"))
        .willReturn(aResponse()
        .withStatus(200)
        .withFixedDelay(2000)));

        assertThrows(Exception.class, () -> authenticateUser.authenticate());
    }
}