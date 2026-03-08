package com.projetos.glpi_worker.service.api_communication;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.projetos.glpi_worker.constants.GlpiHeaderParams;
import com.projetos.glpi_worker.constants.GlpiQueryParams;

import io.netty.handler.timeout.WriteTimeoutException;
import reactor.core.publisher.Flux;


public class TimeoutGetRequest implements ReadOnly {

    private final WebClient webClient;

    public TimeoutGetRequest(WebClient webClient){


        this.webClient = webClient;
    }

    @Override
    public <R> Flux<R> get_request(Class<R> response,  ReadOnlyRequest requestParams) {

        
        this.webClient.get().uri(builder -> {

            builder.path(requestParams.url());

            if(requestParams.filter() != null){

                builder.queryParam(GlpiQueryParams.FILTER.toString(), requestParams.filter());
            }

            return builder.build();
        });



        return null;

    }

}
