package com.pje.kelompok4.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public interface UserRes {
    
    public String getId();

    public String getUsername();

    public String getNik();

    public String getRole();

    public String getCreatedAt();

    public String getUpdatedAt();

}
