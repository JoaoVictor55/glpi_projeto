package com.projetos.glpi_worker.service.api_communication;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.projetos.glpi_worker.constants.ErrorMessages;
import com.projetos.glpi_worker.constants.GlpiHeaderParams;
import com.projetos.glpi_worker.constants.GlpiQueryParams;
import com.projetos.glpi_worker.service.api_authentication.GlpiConnectionProperties;

import io.netty.handler.timeout.WriteTimeoutException;
import reactor.core.publisher.Flux;


@Component
public class TimeoutGetRequest implements ReadOnly {

    private final WebClient webClient;

    //private String baseUrl = "http://localhost";
    //private String glpiEndpoint = "/api.php";

    private String baseUrl;
    private String glpiEndpoint;
    
    public TimeoutGetRequest(GlpiConnectionProperties glpiConnectionProperties){

        this.baseUrl = glpiConnectionProperties.url();
        this.glpiEndpoint = glpiConnectionProperties.apiEndpoint();
        this.webClient = WebClient.builder().baseUrl(baseUrl+glpiEndpoint).build();
    }

    @Override
    public <R> Flux<R> get_request(Class<R> response,  ReadOnlyRequest requestParams) {

        return this.webClient.get().uri(builder -> {

            builder.path(requestParams.url());

            if(requestParams.filter() != null){

                builder.queryParam(GlpiQueryParams.FILTER.toString(), requestParams.filter());
            }

            if(requestParams.start() != null){

                builder.queryParam(GlpiQueryParams.START.toString(), requestParams.start());
            }

            if(requestParams.limit() != null){

                builder.queryParam(GlpiQueryParams.LIMIT.toString(), requestParams.limit());
            }



            return builder.build();
        })
         .header(GlpiHeaderParams.AUTHORIZATION.toString(), GlpiHeaderParams.BEARER.toString()+" "+requestParams.token())
         .accept(MediaType.APPLICATION_JSON)
         .retrieve()
         .bodyToFlux(response)
         .timeout(Duration.ofSeconds(requestParams.timeout()))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException(
            ErrorMessages.TIME_OUT_REQUEST+": "+requestParams.url(), ex
        ));
        //existe um retry, porém é bom deixar quem a chamou cuidar disso :)

    }

}
