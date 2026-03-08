package com.projetos.glpi_worker.service.api_communication;

public record ReadOnlyRequest(
    String url, String token, int timeout,
    String filter, String start, String limit, 
    String id

) {

}
