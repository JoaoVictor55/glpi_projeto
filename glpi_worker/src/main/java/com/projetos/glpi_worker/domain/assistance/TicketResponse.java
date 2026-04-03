package com.projetos.glpi_worker.domain.assistance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.projetos.glpi_worker.domain.administration.Category;
import com.projetos.glpi_worker.domain.administration.Entity;
import com.projetos.glpi_worker.domain.administration.Location;
import com.projetos.glpi_worker.domain.administration.RequestType;
import com.projetos.glpi_worker.domain.administration.Sla;
import com.projetos.glpi_worker.domain.administration.Status;
import com.projetos.glpi_worker.domain.administration.TeamMember;
import com.projetos.glpi_worker.domain.administration.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TicketResponse(
    Integer id,
    String name,
    String content,
    
    @JsonProperty("user_recipient") User recipient,
    @JsonProperty("user_editor") User editor,
    @JsonProperty("is_deleted") boolean isDeleted,
    
    Category category,
    Location location,
    
    Integer urgency,
    Integer impact,
    Integer priority,
    
    @JsonProperty("date_creation") String dateCreation,
    @JsonProperty("date_mod") String dateMod,
    @JsonProperty("date_solve") String dateSolve,
    @JsonProperty("date_close") String dateClose,
    
    @JsonProperty("request_type") RequestType requestType,
    @JsonProperty("status") Status status,
    @JsonProperty("entity") Entity entity,
    
    List<TeamMember> team,
    
    // SLAs e OLAs
    @JsonProperty("sla_ttr") Sla slaTtr,
    @JsonProperty("sla_tto") Sla slaTto,
    @JsonProperty("waiting_duration") Integer waitingDuration
) {}