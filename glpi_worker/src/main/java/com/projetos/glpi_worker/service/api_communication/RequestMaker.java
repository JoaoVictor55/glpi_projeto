package com.projetos.glpi_worker.service.api_communication;

import java.util.Map;

import reactor.core.publisher.Flux;

public interface RequestMaker {

     void deleteRequest(String endpoint, String token, int timeout,  Map<String, String> params, Object ... pathVariables);
    
     <R> Flux<R> getRequest(Class<R>  response, String endPoint, String token,
        int timeout, Map<String, String> params,  Object ... pathVariables); 
    
      <R, P> Flux<R> postRequest(P requestBody, Class<R> response,  String endpoint, String token, int timeout, Map<String, String> params, Object ... pathVariables);
    
     <R, P> Flux<R> patchRequest(P requestBody, Class<R> response, String endPoint, String token,int timeout, 
      Map<String, String> params, Object ...pathVariables);
}
