package com.projetos.glpi_worker.IntegrationAuthenticate;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projetos.glpi_worker.domain.assets.Computer;
import com.projetos.glpi_worker.service.api_authentication.AuthenticateWithPassword;
import com.projetos.glpi_worker.service.api_authentication.TokenResponse;
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
        String endPoint = "Assets/Computer";
        int timeout = 15;

        //asseguramos que conseguimos fazer a autenticação
        TokenResponse tokenResponse = authUser.authenticate(3);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";

        System.out.println("Token gerado: " + tokenResponse.access_token());

        Map<String, String> getRequestParams = Map.of(

            "limit", Integer.toString(limit)
        );

        Flux<Computer> response = timeoutGetRequest.get_request(Computer.class, endPoint, tokenResponse.access_token(),timeout, getRequestParams);
            
        StepVerifier.create(response)
        .expectNextCount(limit)
        .expectComplete()
        .verify();
    }

    //faz um get com um filtro baseado num nome
    @Test
    void authenticateFilterAndGetAsset(){
        
        int limit = 1;
        String value = "PC-FIN-001";
        String rsqlString = "name=="+value;
        String endPoint = "/Assets/Computer";
        int timeout = 15;

        TokenResponse tokenResponse = authUser.authenticate(3);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";

        System.out.println("Token gerado: " + tokenResponse.access_token());

        
        Map<String, String> getRequestParams = Map.of(

            "limit", Integer.toString(limit),
            "filter", rsqlString
        );

        Flux<Computer> response = timeoutGetRequest.get_request(Computer.class, 
            endPoint, tokenResponse.access_token(), timeout,
            getRequestParams
        );

        StepVerifier.create(response)
        .expectNextMatches(computer -> computer.getName().equals(value))
        .expectComplete()
        .verify();

    }

    @Test
    void authenticateAndGetAssetById(){
        
        String endPoint = "/Assets/Computer/{id}";
        int timeout = 15;
        int id = 1;

        TokenResponse tokenResponse = authUser.authenticate(3);
        assert !tokenResponse.access_token().isEmpty() : "Token de autenticação deve ser gerado";

        Flux<Computer> response = timeoutGetRequest.get_request(Computer.class, endPoint, tokenResponse.access_token(), timeout, null, id);

        StepVerifier.create(response)
        .expectNextMatches(computer -> computer.getId().equals(Integer.toString(id)))
        .expectComplete()
        .verify();


    }
}
