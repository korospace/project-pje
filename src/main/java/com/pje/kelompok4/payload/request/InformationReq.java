package com.pje.kelompok4.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class InformationReq {
    @NotBlank(message = "nama info harus diisi")
    @Size(max = 50)
    private String name;
    
    @NotBlank(message = "deskripsi info harus diisi")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
