package com.projetos.glpi_worker.service.api_communication;

public record WriteOnlyRequest(

     String timeout, Boolean isRecursive, Integer entity, String token, String id
) {

}
