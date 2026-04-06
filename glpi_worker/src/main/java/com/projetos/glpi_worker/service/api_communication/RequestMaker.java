package com.projetos.glpi_worker.service.api_communication;

import java.util.Map;

import reactor.core.publisher.Flux;

public interface RequestMaker {

     void deleteRequest(String endpoint, String token, int timeout, int id, Map<String, String> params, Object ... pathVariables);
    
     <R> Flux<R> get_request(Class<R>  response, String endPoint, String token,
        int timeout, Map<String, String> params,  Object ... pathVariables); 
    
      <R, P> R post_request(P requestBody, String endpoint, String token, int timeout, Map<String, String> params, Object ... pathVariables);
    
     <R, P> R patch_request(P requestBody, String endPoint, String token,int timeout, 
      int idToPatch, Map<String, String> params, Object ...pathVariables);
}
