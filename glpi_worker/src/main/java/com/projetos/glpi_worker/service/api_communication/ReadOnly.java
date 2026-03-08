package com.projetos.glpi_worker.service.api_communication;

import reactor.core.publisher.Flux;

//interface simples para classes que só leem. 
public interface ReadOnly {

    <R> Flux<R> get_request(Class<R>  response, ReadOnlyRequest requestParams); 

    

}
