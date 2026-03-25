package com.projetos.glpi_worker.service.api_communication;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.projetos.glpi_worker.constants.ErrorMessages;
import com.projetos.glpi_worker.constants.GlpiHeaderParams;
import com.projetos.glpi_worker.constants.GlpiQueryParams;
import com.projetos.glpi_worker.service.api_authentication.GlpiConnectionProperties;

import io.netty.handler.timeout.WriteTimeoutException;
import reactor.core.publisher.Flux;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

@Component
public class TimeoutRequestMaker implements RequestMaker {


    private final WebClient webClient;

    //private String baseUrl = "http://localhost";
    //private String glpiEndpoint = "/api.php";

    private String baseUrl;
    private String glpiEndpoint;
    
    public TimeoutRequestMaker(GlpiConnectionProperties glpiConnectionProperties){

        this.baseUrl = glpiConnectionProperties.url();
        this.glpiEndpoint = glpiConnectionProperties.apiEndpoint();

        this.webClient = WebClient.builder().baseUrl(baseUrl+glpiEndpoint).build();
    }


    @Override
    public <R> R deleteRequest(int timeout, int id_to_delete, int subId_to_delete, Boolean isRecursive,
            Integer entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRequest'");
    }

    @Override
    public <R> Flux<R> get_request(Class<R>  response, String endPoint, String token, int timeout, Map<String, String> params,
         Object ... pathVariables
    ) {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        if(params != null)
            multiValueMap.setAll(params);

        return this.webClient.get().uri( uriBuilder -> {
   
                uriBuilder.path(endPoint);

                if(!multiValueMap.isEmpty())
                    uriBuilder.queryParams(multiValueMap);


                return uriBuilder.build(pathVariables);}
                )

         .header(GlpiHeaderParams.AUTHORIZATION.toString(), GlpiHeaderParams.BEARER.toString()+" "+token)
         .accept(MediaType.APPLICATION_JSON)
         .retrieve()
         .bodyToFlux(response)
         .timeout(Duration.ofSeconds(timeout))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException(
            ErrorMessages.TIME_OUT_REQUEST+": "+endPoint, ex
        ));
        //existe um retry, porém é bom deixar quem a chamou cuidar disso :)

    }

    @Override
    public <R, P> R post_request(P bodyParams, WriteOnlyRequest requestParams) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'post_request'");
    }

    @Override
    public <R, P> R patch_request(P bodyParams, int timeout, int idToPatch, Boolean isRecursive, Integer entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'patch_request'");
    }

}
