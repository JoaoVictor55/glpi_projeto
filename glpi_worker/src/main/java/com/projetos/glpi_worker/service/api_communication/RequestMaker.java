package com.projetos.glpi_worker.service.api_communication;

import java.util.Map;

import reactor.core.publisher.Flux;

public interface RequestMaker {

     <R> R deleteRequest(int timeout, int id_to_delete, int subId_to_delete, Boolean isRecursive, Integer entity);
     <R> Flux<R> get_request(Class<R>  response, String endPoint, String token, int timeout, Map<String, String> params); 
     <R, P> R post_request(P bodyParams, WriteOnlyRequest requestParams);
    <R, P> R patch_request(P bodyParams, int timeout, int idToPatch, Boolean isRecursive, Integer entity);
}
