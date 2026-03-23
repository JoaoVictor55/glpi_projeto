package com.projetos.glpi_worker.IntegrationAuthenticate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.projetos.glpi_worker.domain.assets.Computer;
import com.projetos.glpi_worker.service.api_authentication.AuthenticateWithPassword;
import com.projetos.glpi_worker.service.api_authentication.TokenResponse;
import com.projetos.glpi_worker.service.api_communication.ReadOnlyRequest;
import com.projetos.glpi_worker.service.api_communication.TimeoutRequestMaker;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class GetRequestTest {

    @Autowired
    private AuthenticateWithPassword authUser;
    @Autowired
    private TimeoutRequestMaker timeoutGetRequest;

    //faz uma requisição get e solicita um número de asset, verificando se o número solicitado chega.
    @Test
    void authenticateAndGetAsset(){

        int limit = 3;

        //asseguramos que conseguimos fazer a autenticação
        TokenResponse tokenResponse = authUser.authenticate(3);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";

        //requisita três computadores
        ReadOnlyRequest params = new ReadOnlyRequest(
            "/Assets/Computer", tokenResponse.access_token(), 15,
            null, null, Integer.toString(limit), 
            null);

        System.out.println("Token gerado: " + tokenResponse.access_token());

        
        Flux<Computer> response = timeoutGetRequest.get_request(Computer.class, params);
            
        StepVerifier.create(response)
        .expectNextCount(limit)
        .expectComplete()
        .verify();
    }

    @Test
    void authenticateFilterAndGetAsset(){

        int limit = 1;
        String value = "PC-FIN-001";
        String rsqlString = "name=="+value;


        TokenResponse tokenResponse = authUser.authenticate(3);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";

        ReadOnlyRequest params = new ReadOnlyRequest(
            "/Assets/Computer", tokenResponse.access_token(), 15,
            rsqlString, null, Integer.toString(limit), 
            null);

        System.out.println("Token gerado: " + tokenResponse.access_token());

        Flux<Computer> response = timeoutGetRequest.get_request(Computer.class, params);

        StepVerifier.create(response)
        .expectNextMatches(computer -> computer.getName().equals(value))
        .expectComplete()
        .verify();

    }
}
