package com.projetos.glpi_worker.service.api_communication;

public interface WriterOnly {

    <R, P> R post_request(P bodyParams, int timeout, Boolean isRecursive, Integer entity);
    <R, P> R patch_request(P bodyParams, int timeout, int idToPatch, Boolean isRecursive, Integer entity);
    

}
