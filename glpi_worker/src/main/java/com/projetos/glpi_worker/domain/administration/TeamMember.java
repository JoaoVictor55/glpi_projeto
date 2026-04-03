package com.projetos.glpi_worker.domain.administration;

public record TeamMember(
    Integer id, 
    String name, 
    String type, 
    String role
) {}