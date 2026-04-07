package com.projetos.glpi_worker.service.api_communication;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.projetos.glpi_worker.constants.ErrorMessages;
import com.projetos.glpi_worker.constants.GlpiHeaderParams;

import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import reactor.core.publisher.Flux;

@Component
public class TimeoutRequestMaker implements RequestMaker {


    private final WebClient webClient;

    @Autowired
    public TimeoutRequestMaker(WebClient webClient){

        this.webClient = webClient;
    }

    @Override
    public <R> Flux<R> get_request(Class<R>  response, String endPoint, String token, int timeout, Map<String, String> params,
         Object ... pathVariables
    ) {


        return this.webClient.get().uri( uriBuilder -> {
   
                uriBuilder.path(endPoint);

                if(params != null){

                    MultiValueMap<String, String> buffer = new LinkedMultiValueMap<>();
                    buffer.setAll(params);
                    uriBuilder.queryParams(buffer);
                }


                return uriBuilder.build(pathVariables);}
                )

         .header(GlpiHeaderParams.AUTHORIZATION.toString(), GlpiHeaderParams.BEARER.toString()+" "+token)
         .accept(MediaType.APPLICATION_JSON)
         .retrieve()
         .bodyToFlux(response)
         .timeout(Duration.ofSeconds(timeout))
        .onErrorMap(ReadTimeoutException.class, ex -> new RuntimeException(
            ErrorMessages.TIME_OUT_REQUEST+": "+endPoint, ex
        ));
        //existe um retry, porém é bom deixar quem a chamou cuidar disso :)
        
    }

    @Override //delete não retorna nada e uma exceção será lançada caso o código não seja de sucesso
    public void deleteRequest(String endpoint, String token, int timeout, int id, Map<String, String> params,Object... pathVariables) {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        

        if (params != null){
            multiValueMap.setAll(params);
        }

        this.webClient.delete().uri(uriBuilder -> {

            uriBuilder.path(endpoint);

            if(params != null){
                uriBuilder.queryParams(multiValueMap);
            }
            
            return uriBuilder.build(pathVariables);

        }).header(GlpiHeaderParams.AUTHORIZATION.toString(), GlpiHeaderParams.BEARER.toString()+" "+token)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(Void.class)
        .timeout(Duration.ofSeconds(timeout))
        .onErrorMap(ReadTimeoutException.class, ex -> new RuntimeException(
            ErrorMessages.TIME_OUT_REQUEST+": "+endpoint, ex));
        
    }

    @Override
    public <R, P> Flux<R> post_request(P requestBody,Class<R> response, String endpoint, String token, int timeout, Map<String, String> params,
            Object... pathVariables) {
        
            
            return this.webClient.post().uri( uriBuilder -> {
   
                uriBuilder.path(endpoint);

                if(params != null){

                    MultiValueMap<String, String> buffer = new LinkedMultiValueMap<>();
                    buffer.setAll(params);
                    uriBuilder.queryParams(buffer);
                }


                return uriBuilder.build(pathVariables);}
            )

         .header(GlpiHeaderParams.AUTHORIZATION.toString(), GlpiHeaderParams.BEARER.toString()+" "+token)
         .bodyValue(requestBody)
         .retrieve()
         .bodyToFlux(response)
         .timeout(Duration.ofSeconds(timeout))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException(
            ErrorMessages.TIME_OUT_REQUEST+": "+endpoint, ex));

    }

    @Override
    public <R, P> Flux<R> patch_request(P requestBody, Class<R> response,  String endPoint, String token, int timeout, int idToPatch,
            Map<String, String> params, Object... pathVariables) {
            
                return this.webClient.patch().uri( uriBuilder -> {
   
                uriBuilder.path(endPoint);

                if(params != null){

                    MultiValueMap<String, String> buffer = new LinkedMultiValueMap<>();
                    buffer.setAll(params);
                    uriBuilder.queryParams(buffer);
                }


                return uriBuilder.build(pathVariables);}
            )

         .header(GlpiHeaderParams.AUTHORIZATION.toString(), GlpiHeaderParams.BEARER.toString()+" "+token)
         .bodyValue(requestBody)
         .retrieve()
         .bodyToFlux(response)
         .timeout(Duration.ofSeconds(timeout))
        .onErrorMap(WriteTimeoutException.class, ex -> new RuntimeException(
            ErrorMessages.TIME_OUT_REQUEST+": "+endPoint, ex));
    }

}
