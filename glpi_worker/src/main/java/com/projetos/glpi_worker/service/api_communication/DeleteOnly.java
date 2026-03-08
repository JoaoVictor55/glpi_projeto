package com.projetos.glpi_worker.service.api_communication;

public interface DeleteOnly {

    <R> R deleteRequest(int timeout, int id_to_delete, int subId_to_delete, Boolean isRecursive, Integer entity);

}
