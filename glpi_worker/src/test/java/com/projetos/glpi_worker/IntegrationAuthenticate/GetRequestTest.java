package com.projetos.glpi_worker.IntegrationAuthenticate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.projetos.glpi_worker.domain.assets.Computer;
import com.projetos.glpi_worker.service.api_authentication.AuthenticateWithPassword;
import com.projetos.glpi_worker.service.api_authentication.TokenResponse;
import com.projetos.glpi_worker.service.api_communication.ReadOnlyRequest;
import com.projetos.glpi_worker.service.api_communication.TimeoutGetRequest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class GetRequestTest {

    @Autowired
    private AuthenticateWithPassword authUser;
    @Autowired
    private TimeoutGetRequest timeoutGetRequest;

    @Test
    void authenticateAndGetAsset(){

        //asseguramos que conseguimos fazer a autenticação
        TokenResponse tokenResponse = authUser.authenticate(3);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";

        //requisita três computadores
        ReadOnlyRequest params = new ReadOnlyRequest(
            "/Assets/Computer", tokenResponse.access_token(), 15,
            null, null, "3", 
            null);

        System.out.println("Token gerado: " + tokenResponse.access_token());

        
        Flux<Computer> response = timeoutGetRequest.get_request(Computer.class, params);
            
        StepVerifier.create(response)
        .expectNextCount(3)
        .expectComplete()
        .verify();
    }

}
