package com.projetos.glpi_worker.clients.assistance;

import java.util.Map;
import com.projetos.glpi_worker.constants.AssistanceEndPoints;
import com.projetos.glpi_worker.domain.assistance.TicketResponse;
import com.projetos.glpi_worker.service.api_communication.RequestMaker;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class TicketClient {

    private RequestMaker requestMaker;

    
    public TicketClient(RequestMaker requestMaker){

        this.requestMaker = requestMaker;
    }

    public Mono<TicketResponse> getTicketById(int id, String token, Integer timeout){


        return requestMaker.getRequest(TicketResponse.class, AssistanceEndPoints.TICKET.toString(), token, timeout, null,id).next();
    }
    
    public Flux<TicketResponse> getTickets(int id, String token, Integer timeout, Map<String, String> requestParams){

        return requestMaker.getRequest(TicketResponse.class, AssistanceEndPoints.TICKET.toString(), token, timeout, requestParams);
    }


}
