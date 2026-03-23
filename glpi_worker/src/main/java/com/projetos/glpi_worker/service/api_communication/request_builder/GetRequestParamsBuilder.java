package com.projetos.glpi_worker.service.api_communication.request_builder;

import java.util.HashMap;
import java.util.Map;

import com.projetos.glpi_worker.constants.GlpiQueryParams;

public class GetRequestParamsBuilder {

    private Map<String, String> params;

    public GetRequestParamsBuilder(){

        this.params = new HashMap<String, String>();
    }

    public GetRequestParamsBuilder withPagination(String start, String limit){

        if (start != null){

            this.params.put(GlpiQueryParams.START.toString(), start);
        }
        if(limit != null){

            this.params.put(GlpiQueryParams.LIMIT.toString(), limit);
        }

        return this;
    }

    public GetRequestParamsBuilder withFilter(String filter){
        
        if(filter != null){

            this.params.put(GlpiQueryParams.FILTER.toString(), filter);
        }

        return this;
    }

    public Map<String, String> build(){

        return this.params;
    }

}
