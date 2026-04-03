package com.projetos.glpi_worker.clients.assistance;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projetos.glpi_worker.constants.AssistanceEndPoints;
import com.projetos.glpi_worker.domain.assistance.TicketResponse;
import com.projetos.glpi_worker.service.api_communication.RequestMaker;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Component
public class TicketClient {

    private RequestMaker requestMaker;

    @Autowired
    public TicketClient(RequestMaker requestMaker){

        this.requestMaker = requestMaker;
    }

    public Mono<TicketResponse> getTicketById(String id, String token, Integer timeOut){


        return requestMaker.get_request(TicketResponse.class, AssistanceEndPoints.TICKET.toString(), token, timeOut, null,timeOut).next();
    }
    
    public Flux<TicketResponse> getTickets(){

        return null;
    }

}
