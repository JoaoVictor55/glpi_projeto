package com.projetos.glpi_worker.service.resiliencia;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projetos.glpi_worker.service.api_communication.RequestMaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import reactor.core.publisher.Flux;
import io.github.resilience4j.retry.Retry;

@Component
public class Resilient implements RequestMaker{

    private RequestMaker requestMaker;
    private CircuitBreaker circuitBreaker;
    private Retry retry;

    @Autowired
    public Resilient(RequestMaker requestMaker, CircuitBreaker circuitBreaker, Retry retry){


        this.requestMaker = requestMaker;
        this.circuitBreaker = circuitBreaker;
        this.retry = retry;
    }


    @Override
    public <R> Flux<R> get_request(Class<R> response, String endPoint, String token, int timeout,
            Map<String, String> params, Object... pathVariables) {
        
        return this.requestMaker.get_request(response, endPoint, token, timeout, params, pathVariables)
        .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
        .transformDeferred(RetryOperator.of(retry))
        .onErrorResume(ex ->{

            return Flux.error(ex);
        });
       
    }


    @Override
    public void deleteRequest(String endpoint, String token, int timeout, Map<String, String> params,
            Object... pathVariables) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRequest'");
    }


    @Override
      public <R, P> Flux<R> post_request(P requestBody, Class<R> response,  String endpoint, String token, int timeout, Map<String, String> params, Object ... pathVariables){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'post_request'");
    }


    @Override
    public <R, P> Flux<R> patch_request(P requestBody, Class<R> response, String endPoint, String token,int timeout, 
       Map<String, String> params, Object ...pathVariables) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'patch_request'");
    }



}
